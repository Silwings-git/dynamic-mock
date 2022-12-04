# Dynamic-Mock API 文档



## Mock Handler

Mock Handler是Dynamic-Mock中最核心的部分（以下部分地方简称处理器），每一个Dynamic-Mock代表了一个URI可响应的数据，可执行的任务的集。Mock Handler使用json格式来定义，Dynamic-Mock最终会解析定义Mock handler的json，生成一棵解析树，当符合Mock Handler基础信息中指定的uri的请求到达时，便会将请求信息封装并交给解析数解析执行，最终得到一个预期的结果。

在Web页面中，可以方便的在图形化界面上完成对Mock Handler的创建，如果熟悉结构，也可以切换编辑模式，直接编辑json数据。

**Dynamic Mock 执行流程图：**

![Mock Handler执行流程图2](Api.assets/Mock Handler执行流程图.png)



### 整体结构

Mock hanlder包含4个部分`基础信息`,`自定义参数空间（customizeSpace）`,`响应集（responses）`,`任务集（tasks）`。

```json
{
  "name": "",
  "httpMethods": [],
  "requestUri": "",
  "label": "",
  "delayTime": 0,
  "customizeSpace": {},
  "responses": [],
  "tasks": []
}
```

#### 基础信息

基础信息一共有五个内容,其中处理器名称（name），标签(label)和处理器执行逻辑无关，只用于识别，可以将label设置为功能或模块名。

1. 适用的请求方式集（httpMethods）

   1. 和`requestUri`组合适用，规定了当前处理器可以处理的请求方式。

2. 适用的请求路径（requestUri）

   1. 通过和`httpMethods`组合，规定了当前处理器可以处理的路径。

3. 延迟执行时间（delayTime）

   1. 单位毫秒
   2. 设置当请求方式和请求路径匹配时，处理器延迟多久开始执行逻辑处理。

   

#### 自定义参数空间(customizeSpace)

在自定义空间中允许预设置一些全局的变量，提供给响应集和任务集使用，例如，需要让每次响应和任务中都包含一个相同的code值，即可让响应和任务都从自定义参数空间获取同一个key的值。

自定义空间为key:value结构，key值不允许重复。

示例：

```json
{
  "productCode": "PC001",
  "skuCodeList": [
    "SC001",
    "SC002"
  ],
  "baseUser": {
    "code": "UC001",
    "name": "Misaka Mikoto",
    "level": 5
  }
}
```



#### 响应集(responses)

响应集中声明的响应信息是最终通过Http响应给客户端的数据内容。可以在响应集中配置多个响应信息，通过条件动态判断哪一条响应信息用于最终返回，如果有多个响应信息的条件集都符合要求，优先使用第一个。响应信息中允许设置一个延迟时间，用来模拟耗时操作。

响应集及响应信息结构：

```json
[
  {
    "name": "Result Body",
    "support": [
      "1+1==2",
      "${#search('baseUser.level') >= 5}"
    ],
    "delayTime": 0,
    "response": {
      "status": 200,
      "headers": {
        "Content-Type": [
          "application/json;charset=UTF-8"
        ],
        "Date": [
          "Wed, 09 Nov 2022 21:25:02 GMT"
        ]
      },
      "body": {
        "name": "${#search('name')}",
        "age": "${#random('int',100)}"
      }
    }
  }
]
```

**字段说明：**

1. 响应名称（name）

   1. 仅用于标识

2. 适用条件集（support）

   1. 条件集中允许多个布尔表达式，只有当条件集中全部的布尔表达式结构为true时，该响应信息才能用于对应处理器的响应信息。
   2. 当集合中不包含元素时默认整体结果为true
   3. 支持***动态表达式***

3. 延迟响应时间（delayTime）

   1. 单位毫秒
   2. 设置当前响应信息适用时，延迟多久向客户端响应数据。
   3. 不支持***动态表达式***

4. 响应信息（response）：

   http的响应信息，支持***动态表达式***。

   1. HTTP响应码（status）
      1. 设置响应时的HTTP响应码
      2. 必须是数值或可转换为数值的类型
   2. HTTP响应头（headers）
      1. 设置响应时的HTTP响应头
      2. 需要注意的是，headers中的value必须是集合格式。
   3. HTTP响应体（body）
      1. 设置响应的响应体。可以是json或字符格式



#### 任务集(tasks)

任务集中可设置多个发起http请求的任务。和响应集类似，任务集中的任务也通过条件来控制是否需要触发，当条件集中的布尔表达式全部返回true时表示该任务需要执行，不同的是，任务集中满足条件的任务都会得到执行，而不是只有第一个会执行。

任务分为同步任务和异步任务两类，除了都是发起一个Http请求外，有着很大的不同。

1. 同步任务并不是一个定时任务。它会忽略cron表达式和限次参数，而是在Mock Handler的执行过程中，同步的发起一次请求，请求完成后，才会继续Mock Handler的其他逻辑处理（因此如果响应信息中设置了延迟时间，其真实延迟的时间除了Mock Handler本身的执行耗时，还需要加上任务的http请求耗时）。
2. 异步任务是定时任务。当确认执行时会被注册到定时任务管理器中，并立即开始异步执行（此时Mock handler的执行可能尚未全部完成）。定时任务中使用cron表达式指定执行周期，同时可以设置执行次数，在达到执行次数后自动关闭定时任务，当然，除了自动关闭定时任务，也可以Web页面通过任务管理手动关闭。

任务信息结构：

```json
[
  {
    "name": "Http Task",
    "support": [
      "${#equals(#search('baseUser.name','customizeSpace'),'Misaka Mikoto')}"
    ],
    "async": false,
    "cron": "* * * * * ?",
    "numberOfExecute": "1",
    "request": {
      "requestUrl": "http://localhost:8088/hello/word",
      "httpMethod": "POST",
      "headers": {
        "authToken": [
          "8888888888"
        ]
      },
      "body": {
        "timestemp": "${#now()}",
        "name": "${#search('baseUser.name')}",
        "id": "${#uuid()}"
      },
      "uriVariables": {
        "timestemp": [
          "${#now()}"
        ],
        "index": [
          "1",
          "2"
        ]
      }
    }
  }
]
```

**字段说明**

1. 任务名称（name）
   
   1. 仅用于标识
   
2. 适用条件集（support）
   1. 条件集中允许多个布尔表达式，只有当条件集中全部的布尔表达式结构为true时，该响应信息才能用于对应处理器的响应信息。
   2. 当集合中不包含元素时默认整体结果为true
   3. 支持***动态表达式***
   
3. 是否异步（async）
   1. true-异步执行，false-同步执行
   2. 仅支持布尔类型
   3. 不支持***动态表达式***
   
4. cron表达式（cron）
   1. 用于设置异步任务的执行周期
   2. 不支持***动态表达式***
   
5. 执行次数（numberOfExecute）
   1. 用于设置异步任务的执行次数
   2. 不支持***动态表达式***
   
6. 请求信息（request）
   
   请求信息部分，支持***动态表达式***
   
   1. 请求地址（requestUrl）
      1. 设置要发起请求的地址，可以不带http，会自动添加
   2. 请求方式（httpMethod）
      1. 设置发起请求的请求方式，仅支持`GET`,`POST`,`PUT`,`DELETE`
   3. 请求头（headers）
      1. 设置发起的请求的请求头，注意，value部分需要使用集合
   4. 请求体（body）
      1. 设置发起的请求的请求体
   5. URI参数（uriVariables）
      1. 设置发起的请求的URI参数，注意，value部分需要使用集合





## 动态表达式

动态表达式是一段以`${`开头,`}`结尾进行声明的文本内容，`${}`内的内容为表达式的内容。它是Mock Handler实现动态逻辑的核心，表达式在运行时基于上下文信息进行计算，不同的上下文能得到不同的结果。

动态表达式被分为函数表达式和逻辑表达式。函数表达式由函数声明和函数参数组成，例如`#search('code','customizespace')`,search表示搜索函数，'code','customizespace'则为调用该函数时传递的两个参数。非函数表达式的的动态表达式均认为是逻辑表达式。



### 函数表达式

语法：

1. 函数表达式固定写法`#函数名(参数列表)`，示例：#search('code','customizespace')，#concat(1,2)，#uuid()
2. 函数名不区分大小写
3. 参数列表使用英文逗号分隔
4. 单个参数可以使用动态表达式
5. 参数中的字符类型必须添加英文单引号



##### 函数列表

*以下函数说明中，参数类型限制，表示运行时的实际类型限制，并非在声明时只能使用json表示的类型。例如参数类型为Boolean，声明时可使用isBlank函数，因为isBlank函数的实际返回值类型为Boolean，符合要求。另外，对于参数类型限制为数值类型或Boolean类型的，如果运行时得到的参数不是所需类型，也会优先尝试进行类型转换。

1. Concat

   用于将参数拼接的函数

   1. 最小参数数量：0（为0时默认为空串。）

   2. 最大参数数量：不限

   3. 参数类型限制：不限

   4. 返回值类型：String

   5. 示例：

      1. #concat(1,2,3) => 123

      2. #concat('Java',' ','Good',' ','!') => Java Good !

      3. #concat() => 

         

2. Equals

   比较两个参数内容转换为String后是否完全一致，可简写为`eq`。

   1. 最小参数数量：2

   2. 最大参数数量：2

   3. 参数类型限制：不限

   4. 返回值类型：Boolean

   5. 示例：

      1. #equals(1,1) => true

      2. #eq(1,2) => false

      3. #eq(1,1-1) => false

         

3. IsBlank

   判断参数转换为String后是否为空或去除空白符号后长度为0（参考Java `org.apache.commons.lang3.StringUtils#isBlank`）

   1. 最小参数数量：0（为0时表示默认为空串。）

   2. 最大参数数量：1

   3. 参数类型限制：不限

   4. 返回值类型：Boolean

   5. 示例：

      1. #isBlank(1) => false

      2. #isBlank('1 ') => false

      3. #isBlank('  ') => true

         

4. Join(delimiter，String...)

   使用分隔符连接多个参数的字符串形式，可类比 Java `java.lang.String#join(java.lang.CharSequence, java.lang.Iterable<? extends java.lang.CharSequence>)`

   1. 最小参数数量：2

   2. 最大参数数量：不限

   3. 参数说明：

      1. delimiter：分隔符
      2. String...:不限数量个可转换为String的内容。
      3. Join方法的参数在运行时有点特殊，其第二个参数在声明时必须为字符类型，但如果将第二个参数声明为函数，则在运行时有可能得到的集合类型，当第二个参数是集合类型时，将遍历集合进行join。但手动声明时不支持使用`[]`声明一个集合，这样的数据会被直接视为字符串，如有需要，应当作为多个参数传递。

   4. 返回值类型：String

   5. 示例：

      1. #join(<'-'>,1,2,3) => 1-2-3

      2. #join('#',1,,2,,3) => 1##2##3

      3. #join('',1,2,3) => 123

      4. #join('#',) => 

         

5. Now

   生成时间戳，有两个重载函数

   1. Now()
      1. 最小参数数量：0
      2. 最大参数数量：0
      3. 返回值类型：Long
      4. 示例：
         1. #now() => 1670154841950
   2. Now(format)
      1. 最小参数数量：1
      2. 最大参数数量：1
      3. 参数类型限制：
         1. 必须是有效的时间格式
      4. 返回值类型：String
      5. 示例：
         1. #now(<'yyyy-MM-dd'>) => 2022-12-04
         2. #now(<'yyyy-MM-dd HH:mm:ss'>) => 2022-12-04 20:05:25
         3. #now('yyyy') => 2022

6. PageData

7. Random

8. Search

9. UUID

10. 陆续更新中。。。





### 逻辑表达式
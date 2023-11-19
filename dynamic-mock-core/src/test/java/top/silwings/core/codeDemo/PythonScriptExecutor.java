package top.silwings.core.codeDemo;

//import org.python.util.PythonInterpreter;

public class PythonScriptExecutor {
//    public static void main(String[] args) {
//        // 执行 JavaScript 代码并获取函数引用
//        final String script = "def modify_username(user):\n" +
//                              "    print(user.getName())\n" +
//                              "    user.setName('小王')\n" +
//                              "    print(user.getName())\n" +
//                              "    return user\n";
//
//        System.setProperty("python.console.encoding", "UTF-8");
//
//        try (final PythonInterpreter interpreter = new PythonInterpreter()) {
//
//            // 执行 Python 脚本
//            interpreter.exec(script);
//
//            // ScriptInterfaceType可以保证拿到的class一定是函数式接口
//            final Method method = ModifyNmae.class.getDeclaredMethods()[0];
//
//            final ModifyNmae modifyNmae = interpreter.get(method.getName(), ModifyNmae.class);
//
//            final Person newPerson = modifyNmae.modify_username(new Person("张三"));
//
//            System.out.println("newPerson.getName() = " + newPerson.getName());
//        }
//    }

    public static interface ModifyNmae {
        public Person modify_username(Person person);
    }

    public static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }
}

package top.silwings.admin.auth.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName PermissionLimit
 * @Description 权限控制
 * @Author Silwings
 * @Date 2022/11/19 12:44
 * @Since
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {

    /**
     * 登录拦截 (默认拦截)
     */
    boolean limit() default true;

    /**
     * 要求管理员权限
     */
    boolean adminUser() default false;

}

package com.sky.skydemo.datasource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 *
 * @author
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@Import(AspectJAutoProxyRegistrar.class)
public @interface SwitchDataSource {
    String value() default "";
}

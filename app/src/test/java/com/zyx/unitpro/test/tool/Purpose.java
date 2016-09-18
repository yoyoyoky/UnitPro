package com.zyx.unitpro.test.tool;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhangyouxuan on 2016/5/23.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Purpose {
    String desc() default "";
}

package com.pro.persist.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by JackYang on 2017/6/29.
 * 布局注解
 */

@Target(ElementType.TYPE)         //元素类型
@Retention(RetentionPolicy.RUNTIME)   //保留到class
public @interface SuperBindContentView {
    int value();                    //返回布局id
}

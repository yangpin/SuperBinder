package com.pro.persist.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by JackYang on 2017/6/29.
 * 字段注解
 */

@Target(ElementType.FIELD)         //指定元素类型为成员变量
@Retention(RetentionPolicy.CLASS)  //保留到字节码
public @interface SuperBindView {
    int value();                    //返回参数为int值 因为需要指定的就是资源id
}

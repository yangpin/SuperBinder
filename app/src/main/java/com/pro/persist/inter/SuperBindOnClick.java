package com.pro.persist.inter;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by JackYang on 2017/6/29.
 * 事件注解
 * 点击事件的注解略显麻烦，我们需要声明其方法名字，事件名字，方法类型，等 所以需要写一个自定义的注解 BaseOnClick
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@BaseOnClick(methodName = "onClick", listener = "setOnClickListener", listenerType = View.OnClickListener.class)
public @interface SuperBindOnClick {
    int[] value();
}

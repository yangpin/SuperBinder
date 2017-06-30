package com.pro.persist.parse;

import android.app.Activity;
import android.view.View;

import com.pro.persist.annotationknife.inter.BaseOnClick;
import com.pro.persist.annotationknife.inter.SuperBindContentView;
import com.pro.persist.annotationknife.inter.SuperBindOnClick;
import com.pro.persist.annotationknife.inter.SuperBindView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Created by JackYang on 2017/6/29.
 * superBind  解析类
 */

public class SuperBind {

    //  方法名
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";
    private static final String METHOD_SET_CONTENT_VIEW = "setContentView";
    private static final String METHOD_ON_CLICK = "onClick";


    public static void bindView(Activity activity) {
        //获取activity  的class
        Class<? extends Activity> clazz = activity.getClass();
        //所有属性
        Field[] fields = clazz.getDeclaredFields();
        //遍历
        for (Field f : fields) {
            //拿到SuperBind 从而获取想要的id
            SuperBindView bind = f.getAnnotation(SuperBindView.class);
            if (bind != null) {
                int id = bind.value();
                if (id != -1) {
                    try {
                        Method fvbMethod = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        try {

                            Object mView = fvbMethod.invoke(activity, id);
                            f.setAccessible(true);
                            f.set(activity, mView);

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * setContentView
     *
     * @param activity
     */
    public static void bindContentView(Activity activity) {
        //获取activity  的class
        Class<? extends Activity> clazz = activity.getClass();
        //方法的注解从class中获取
        SuperBindContentView superBindContentView = clazz.getAnnotation(SuperBindContentView.class);
        if (superBindContentView != null) {
            int value = superBindContentView.value();
            if (value != -1) {
                try {
                    Method scvMethod = clazz.getMethod(METHOD_SET_CONTENT_VIEW, int.class);
                    scvMethod.setAccessible(true);//激活
                    try {
                        scvMethod.invoke(activity, value);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析事件的注解
     *
     * @param activity
     */
    public static void bindOnClick(Activity activity) {
        //获取注解类
        Class<? extends Activity> clazz = activity.getClass();
        //获取所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //得到被OnClick 注解的方法
            if (method.isAnnotationPresent(SuperBindOnClick.class)) {
                SuperBindOnClick superBindOnClick = method.getAnnotation(SuperBindOnClick.class);
                //注解的值
                int[] ids = superBindOnClick.value();
                //获取baseOnClick 注解  根据注解获取注解
                BaseOnClick baseOnClick = superBindOnClick.annotationType().getAnnotation(BaseOnClick.class);
                //获取baseOnClick注解的值
                Class<?> listenerType = baseOnClick.listenerType();
                String listener = baseOnClick.listener();
                String methodName = baseOnClick.methodName();
                //这里需要用到动态代理 关于动态代理 下文详细介绍
                ProxyHandler proxyHandler = new ProxyHandler(activity);
                //指定代理什么
                Object proxyListener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, proxyHandler);
                //把方法添加进去
                proxyHandler.addMethod(methodName, method);
                //View  的点击事件
                for (int i :
                        ids) {
                    try {
                        //获取findViewById方法
                        Method findViewByIdMethod = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        findViewByIdMethod.setAccessible(true);
                        try {
                            //获取view
                            View view = (View) findViewByIdMethod.invoke(activity, i);
                            //获取点击事件
                            Method onClickListener = view.getClass().getMethod(listener, listenerType);
                            //对这个点击事件进行操作
                            onClickListener.setAccessible(true);
                            //对象和方法
                            onClickListener.invoke(view, proxyListener);

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }


                }


            }
        }
    }
    /**
     * 动态代理
     */
    static class ProxyHandler implements InvocationHandler {
        //存放方法的map
        private final HashMap<String, Method> methodMAP = new HashMap<>();
        //使用弱引用
        private WeakReference<Object> weakRef;

        //把Activity传进弱引用 以防内存泄漏
        public ProxyHandler(Object obj) {
            this.weakRef = new WeakReference<Object>(obj);
        }

        /**
         * 添加方法
         *
         * @param name
         * @param method
         */
        public void addMethod(String name, Method method) {
            methodMAP.put(name, method);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //获取activity
            Object o = weakRef.get();
            if (o != null) {
                //方法名
                String methodName = method.getName();
                //从map中获取该方法名对应的方法  此处对method进行了替换
                method = methodMAP.get(methodName);
                if (method != null) {
                    //执行
                    method.invoke(o, args);
                }
            }
            return null;
        }
    }


}

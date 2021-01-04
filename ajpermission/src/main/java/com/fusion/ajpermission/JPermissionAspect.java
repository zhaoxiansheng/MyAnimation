package com.fusion.ajpermission;

import android.content.Context;

import com.fusion.ajpermission.annotation.Permission;
import com.fusion.ajpermission.annotation.PermissionDenied;
import com.fusion.ajpermission.annotation.PermissionDeniedForver;
import com.fusion.ajpermission.bean.DenyInfo;
import com.fusion.ajpermission.bean.DenyForeverInfo;
import com.fusion.ajpermission.callback.IPermission;
import com.fusion.ajpermission.utils.JPermissionHelper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jiang fusion
 * @date 创建时间：2018/4/18
 * @description
 */

@Aspect
public class JPermissionAspect {

    private static final String TAG = JPermissionAspect.class.getSimpleName();

    //匹配被@com.fusion.ajpermission.annotation.Permission注解的所有方法 且将参数是permission是注解
    @Pointcut("execution(@com.fusion.ajpermission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {

    }

    @Around("requestPermission(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {

        Context context = null;

        final Object object = joinPoint.getThis();
        if (joinPoint.getThis() instanceof Context) {
            context = (Context) object;
        } else if (joinPoint.getThis() instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else if (joinPoint.getThis() instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else if (object instanceof android.view.View) {
            context = ((android.view.View) object).getContext();
        }

        if (context == null) {
            context = JPermissionHelper.getContext();
        }

        if (context == null || permission == null) {
            return;
        }

        JPermissionActivity.permissionRequest(context, false, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void ganted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void deniedForver(int requestCode, List<String> foreverList) {
                //获取切面上下文的类型
                Class<?> clz = object.getClass();

               while(clz != null) {

                    String clazzName = clz.getCanonicalName();
                    if (clazzName.startsWith("java.") || clazzName.startsWith("android.")) {
                        break;
                    }

                    //获取类型中的方法
                    Method[] methods = clz.getDeclaredMethods();
                    if (methods == null) {
                        continue;
                    }

                    for (Method method : methods) {
                        //获取该方法是否有PermissionDenied注解
                        boolean isHasAnnotation = method.isAnnotationPresent(PermissionDeniedForver.class);
                        if (isHasAnnotation) {
                            method.setAccessible(true);

                            //获取方法的参数类型，确定其可以回传
                            Class<?>[] parameterTypes = method.getParameterTypes();

                            //需要参数长度为1且类型需要为DenyInfo
                            if (parameterTypes == null || parameterTypes.length != 1) {
                                return;
                            } else if (parameterTypes[0] != DenyForeverInfo.class) {
                                return;
                            }

                            DenyForeverInfo denyForeverInfo = new DenyForeverInfo(requestCode, foreverList);

                            PermissionDeniedForver annotation = method.getAnnotation(PermissionDeniedForver.class);
                            int annotationReqCode = annotation.requestCode();

                            try {
                                if (annotationReqCode == JPermissionHelper.DEFAULT_REQUEST_CODE) {  //为默认值时，调用该注视方法
                                    method.invoke(object, denyForeverInfo);
                                } else if (annotationReqCode == requestCode) {    //当设置的回调值与请求值相同时，调用
                                    method.invoke(object, denyForeverInfo);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    clz = clz.getSuperclass();
                }
            }

            @Override
            public void denied(int requestCode, List<String> deniedList) {

                //获取切面上下文的类型
                Class<?> clz = object.getClass();

                while (clz != null){

                    String clazzName = clz.getCanonicalName();
                    if (clazzName.startsWith("java.") || clazzName.startsWith("android.")) {
                        break;
                    }

                    //获取类型中的方法
                    Method[] methods = clz.getDeclaredMethods();
                    if (methods == null) {
                        continue;
                    }

                    for (Method method : methods) {
                        //获取该方法是否有PermissionCanceled注解
                        boolean isHasAnnotation = method.isAnnotationPresent(PermissionDenied.class);
                        if (isHasAnnotation) {
                            method.setAccessible(true);

                            //获取方法的参数类型，确定其可以回传
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            //需要参数长度为1且类型需要为CancelInfo
                            if (parameterTypes == null || parameterTypes.length != 1) {
                                return;
                            } else if (parameterTypes[0] != DenyInfo.class) {
                                return;
                            }

                            DenyInfo denyInfo = new DenyInfo(requestCode, deniedList);

                            PermissionDenied annotation = method.getAnnotation(PermissionDenied.class);
                            int annotationReqCode = annotation.requestCode();

                            try {
                                if (annotationReqCode == JPermissionHelper.DEFAULT_REQUEST_CODE) {  //为默认值时，调用该注视方法
                                    method.invoke(object, denyInfo);
                                } else if (annotationReqCode == requestCode) {    //当设置的回调值与请求值相同时，调用
                                    method.invoke(object, denyInfo);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    clz = clz.getSuperclass();
                }
            }
        });

    }

}

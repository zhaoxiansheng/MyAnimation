package com.example.extension.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * AspectJ, Get action time
 */
@Aspect
public class AspectClass {

    private final String exeLayoutManagerAnimationEnd = "execution(* org.yanzi.camera.test.LayerManager.resizeCanvasMatchSurface(..))";

    /**
     * Around会拦截原方法执行，需要调用proceed执行
     *
     * @param joinPoint
     */
    @Around(exeLayoutManagerAnimationEnd)
    public void aroundActivityOnCreate(ProceedingJoinPoint joinPoint) {
        AspectUtil.makeJoinPoint(joinPoint);
    }


}

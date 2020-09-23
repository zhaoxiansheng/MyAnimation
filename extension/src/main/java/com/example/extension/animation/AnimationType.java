package com.example.extension.animation;

public class AnimationType {
    /**
     * 渐变
     */
    public static final int FADE = 0x0001;

    /**
     * 滑动
     */
    public static final int SLIDE = 0x0002;

    /**
     * fade changebounds 组合动画
     */
    public static final int AUTO_TRANSITION = 0x0003;

    /**
     * 改变目标视图边界
     */
    public static final int CHANGE_BOUNDS = 0x0004;

    /**
     * scale 和 remote
     */
    public static final int CHANGE_TRANSFORM = 0x0005;

    /**
     * 分解
     */
    public static final int EXPLODE = 0x0006;
}

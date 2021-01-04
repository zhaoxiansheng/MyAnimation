package com.autopai.common.bean;

public class ScreenItem {

    public static int TYPE_NORMAL = 0;
    public static int TYPE_HOME_MUSIC = TYPE_NORMAL + 1;
    public static int TYPE_HOME_NAVI = TYPE_NORMAL + 2;
    public static int TYPE_HOME_WEATHER = TYPE_NORMAL + 3;
    public static int TYPE_MUSIC_PLAYING = TYPE_NORMAL + 4;
    public static int TYPE_MUSIC = TYPE_NORMAL + 5;
    public static int TYPE_NAVI_POST = TYPE_NORMAL + 6;
    public static int TYPE_NAVI = TYPE_NORMAL + 7;

    /**
     * view类型
     */
    public int mType;
    public int mState;
    public int mGroupId;
    public int mScreenPage;
    public int mCellX, mCellY, mCellW, mCellH;
    public int mLastCellX, mLastCellY, mLastCellW, mLastCellH;
    public boolean isCustomWidget = true;
    public boolean isSystemWidget = false;
    public boolean isNormalView = false;

    public int unUpdateViewIndex = -1;
    public int weightSum = -1;
    /**
     * pkgName + widgetName
     */
    public String mName;
    /**
     * 0 all
     * 1
     */
    public int transitionType;

    private boolean isFold = false;

    public ScreenItem(int screenPage, int cellX, int cellY, int cellW, int cellH) {
        this.mScreenPage = screenPage;
        this.mCellX = this.mLastCellX = cellX;
        this.mCellY = this.mLastCellY = cellY;
        this.mCellW = this.mLastCellW = cellW;
        this.mCellH = this.mLastCellH = cellH;
    }

    public ScreenItem(String name, int cellX, int cellY, int cellW, int cellH) {
        this.mName = name;
        this.mCellX = this.mLastCellX = cellX;
        this.mCellY = this.mLastCellY = cellY;
        this.mCellW = this.mLastCellW = cellW;
        this.mCellH = this.mLastCellH = cellH;
    }

    public ScreenItem(String name, int cellX, int cellY, int cellW, int cellH, int transitionType) {
        this.mName = name;
        this.mCellX = this.mLastCellX = cellX;
        this.mCellY = this.mLastCellY = cellY;
        this.mCellW = this.mLastCellW = cellW;
        this.mCellH = this.mLastCellH = cellH;
        this.transitionType = transitionType;
    }

    public ScreenItem(String name, int cellX, int cellY, int cellW, int cellH, int transitionType, int weightSum, int unUpdateViewIndex) {
        this.mName = name;
        this.mCellX = this.mLastCellX = cellX;
        this.mCellY = this.mLastCellY = cellY;
        this.mCellW = this.mLastCellW = cellW;
        this.mCellH = this.mLastCellH = cellH;
        this.transitionType = transitionType;
        this.weightSum = weightSum;
        this.unUpdateViewIndex = unUpdateViewIndex;
    }

    public boolean isNavi() {
        return this.mType == TYPE_HOME_NAVI || this.mType == TYPE_NAVI;
    }

    public String getPackageName() {
        String packageName = null;
        if (mName != null) {
            String[] commpont = mName.split("/");
            packageName = commpont[0];
        }
        return packageName;
    }

    public String getClassName() {
        String className = null;
        if (mName != null) {
            String[] commpont = mName.split("/");
            className = commpont[1];
        }
        return className;
    }

    public void setIsFold(boolean isFold) {
        this.isFold = isFold;
    }

    public boolean isFold() {
        return isFold;
    }
}

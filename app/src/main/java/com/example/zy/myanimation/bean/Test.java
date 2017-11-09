package com.example.zy.myanimation.bean;

/**
 * Created on 2017/11/8.
 *
 * @author zhaoy
 */
public class Test {

    private String name;
    private String currentSales;
    private String totalSales;

    public Test(String name, String currentSales, String totalSales) {
        this.name = name;
        this.currentSales = currentSales;
        this.totalSales = totalSales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentSales() {
        return currentSales;
    }

    public void setCurrentSales(String currentSales) {
        this.currentSales = currentSales;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }
}

package com.example.zy.myanimation.utils.shitu;

import java.util.ArrayList;

public class Bean {

    /**
     * log_id : 3516819269386289444
     * result_num : 5
     * result : [{"score":0.694213,"root":"植物-蔷薇科","keyword":"草莓"},{"score":0.541138,"root":"植物-其它","keyword":"红色草莓"},{"score":0.340946,"root":"商品-穿戴","keyword":"领带"},{"score":0.18641,"root":"商品-箱包","keyword":"双肩包"},{"score":0.034288,"root":"商品-户外用品","keyword":"足球鞋"}]
     */

    private long log_id;
    private int result_num;
    private ArrayList<ResultBean> result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public ArrayList<ResultBean> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * score : 0.694213
         * root : 植物-蔷薇科
         * keyword : 草莓
         */

        private double score;
        private String root;
        private String keyword;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
}

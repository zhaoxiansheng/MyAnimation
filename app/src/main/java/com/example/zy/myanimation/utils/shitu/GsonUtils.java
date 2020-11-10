package com.example.zy.myanimation.utils.shitu;

import com.google.gson.Gson;

public class GsonUtils {

    public static Bean resultToJson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, Bean.class);
    }
}

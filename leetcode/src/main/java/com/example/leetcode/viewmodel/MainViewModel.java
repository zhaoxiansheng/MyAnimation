package com.example.leetcode.viewmodel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public final MutableLiveData<List<String>> list = new MutableLiveData<>();
}

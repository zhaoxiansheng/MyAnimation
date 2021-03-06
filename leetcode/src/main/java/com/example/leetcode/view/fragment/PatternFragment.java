package com.example.leetcode.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leetcode.R;
import com.example.leetcode.pattern.DecorationMode;
import com.example.leetcode.pattern.ObserverPattern;
import com.example.leetcode.pattern.StrategyPattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatternFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatternFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PatternFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatternFragment.
     */
    public static PatternFragment newInstance(String param1, String param2) {
        PatternFragment fragment = new PatternFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        StrategyPattern.MallardDuck duck = new StrategyPattern.MallardDuck();
        duck.display();

        DecorationMode.Mocha mocha = new DecorationMode.Mocha(new DecorationMode.HouseBlend());
        String description = mocha.getDescription();
        double cost = mocha.cost();
        Log.d("DecorationMode", "description: " + description + ", cost: " + cost);

        ObserverPattern.WeatherData weatherData = new ObserverPattern.WeatherData();
        ObserverPattern.ThirdPartyDisplay thirdPartyDisplay = new ObserverPattern.ThirdPartyDisplay();

        weatherData.registerObserver(thirdPartyDisplay);

        weatherData.setHumidity(10);
        weatherData.setPressure(100);
        weatherData.setTemperature(23);
        weatherData.notifyObserver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pattern, container, false);
    }
}
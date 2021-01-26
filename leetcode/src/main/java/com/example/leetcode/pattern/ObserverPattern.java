package com.example.leetcode.pattern;

import android.util.Log;

import java.util.ArrayList;

//观察者模式
public class ObserverPattern {

    private static final String TAG = ObserverPattern.class.getSimpleName();

    //观察者
    public interface Observer {
        void update(int temmperature, int humidity, int pressure);
    }

    //被观察者
    public interface Observerable {
        void registerObserver(Observer o);

        void removeObserver(Observer o);

        void notifyObserver();
    }

    public interface Display {
        void display();
    }

    public static class WeatherData implements Observerable {

        private ArrayList<Observer> observers = new ArrayList<>();

        private int temperature;
        private int humidity;
        private int pressure;

        @Override
        public void registerObserver(Observer o) {
            observers.add(o);
        }

        @Override
        public void removeObserver(Observer o) {
            observers.remove(o);
        }

        @Override
        public void notifyObserver() {
            for (Observer o : observers) {
                o.update(temperature, humidity, pressure);
            }
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }
    }

    public static class ThirdPartyDisplay implements Display, Observer {
        private int temperature;
        private int humidity;
        private int pressure;

        @Override
        public void display() {
            Log.d(TAG, "display: temperature = " + temperature + ", humidity: " + humidity + ", pressure: " + pressure);
        }

        @Override
        public void update(int temperature, int humidity, int pressure) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.pressure = pressure;
            display();
        }
    }
}

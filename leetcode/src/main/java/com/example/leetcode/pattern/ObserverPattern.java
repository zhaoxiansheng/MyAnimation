package com.example.leetcode.pattern;

import java.util.ArrayList;

//观察者模式
public class ObserverPattern {

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

    public class WeatherData implements Observerable {

        private ArrayList<Observer> observers = new ArrayList<>();

        private int temmperature;
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
                o.update(temmperature, humidity, pressure);
            }
        }
    }

    public class ThirdPartyDisplay implements Display, Observer {
        private int temmperature;
        private int humidity;
        private int pressure;

        @Override
        public void display() {
            //...
        }

        @Override
        public void update(int temmperature, int humidity, int pressure) {
            this.temmperature = temmperature;
            this.humidity = humidity;
            this.pressure = pressure;
            display();
        }
    }
}

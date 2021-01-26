package com.example.leetcode.pattern;

//策略模式
public class StrategyPattern {

    private static final String TAG = StrategyPattern.class.getSimpleName();

    public static abstract class Duck {

        FlyBehavior flyBehavior;
        QuackBehavior quackBehavior;

        protected abstract void display();

        protected void swim() {
            System.out.println("我会游泳～～～～");
        }

        protected void performQuack() {
            quackBehavior.quack();
        }

        protected void performFly() {
            flyBehavior.fly();
        }
    }

    public static interface FlyBehavior {
        void fly();
    }

    public static interface QuackBehavior {
        void quack();
    }

    public static class FlyWithWings implements FlyBehavior {
        @Override
        public void fly() {
            System.out.println("飞飞飞");
        }
    }

    public static class FlyNoWay implements FlyBehavior {
        @Override
        public void fly() {
            System.out.println("我不会飞");
        }
    }

    public static class Quack implements QuackBehavior {
        @Override
        public void quack() {
            System.out.println("呱呱呱");
        }
    }

    public static class Squeak implements QuackBehavior {
        @Override
        public void quack() {
            System.out.println("吱吱吱");
        }
    }

    public static class MuteQuack implements QuackBehavior {
        @Override
        public void quack() {
            System.out.println("我不会叫");
        }
    }

    public static class MallardDuck extends Duck {

        public MallardDuck() {
            flyBehavior = new FlyWithWings();
            quackBehavior = new Quack();
        }

        @Override
        public void display() {
            flyBehavior.fly();
            quackBehavior.quack();
        }
    }
}

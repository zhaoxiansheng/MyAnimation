package com.example.leetcode.pattern;

//策略模式
public class StrategyPattern {

    private static final String TAG = StrategyPattern.class.getSimpleName();

    public abstract class Duck {

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

    public interface FlyBehavior {
        void fly();
    }

    public interface QuackBehavior {
        void quack();
    }

    public class FlyWithWings implements FlyBehavior {
        @Override
        public void fly() {
            System.out.println("飞飞飞");
        }
    }

    public class FlyNoWay implements FlyBehavior {
        @Override
        public void fly() {
            System.out.println("我不会飞");
        }
    }

    public class Quack implements QuackBehavior {
        @Override
        public void quack() {
            System.out.println("呱呱呱");
        }
    }

    public class Squeak implements QuackBehavior {
        @Override
        public void quack() {
            System.out.println("吱吱吱");
        }
    }

    public class MuteQuack implements QuackBehavior {
        @Override
        public void quack() {
            System.out.println("我不会叫");
        }
    }

    public class MallardDuck extends Duck {

        public MallardDuck() {
            flyBehavior = new FlyWithWings();
            quackBehavior = new Quack();
        }

        @Override
        protected void display() {
            flyBehavior.fly();
            quackBehavior.quack();
        }
    }
}

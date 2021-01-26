package com.example.leetcode.pattern;

//装饰者模式
public class DecorationMode {

    //抽象组件
    public static abstract class Beverage {
        String description = "Unknown Beverage";

        public String getDescription() {
            return description;
        }

        public abstract double cost();
    }

    //抽象装饰者
    public abstract static class CondimentDecorator extends Beverage {
        @Override
        public abstract String getDescription();
    }

    public static class Espresso extends Beverage {

        public Espresso() {
            description = "Espresso";
        }

        @Override
        public double cost() {
            return 1.99;
        }
    }

    //具体组件
    public static class HouseBlend extends Beverage {
        public HouseBlend() {
            description = "House Blend Coffee";
        }

        @Override
        public double cost() {
            return 0.99;
        }
    }

    //具体的装饰者
    public static class Mocha extends CondimentDecorator {
        Beverage beverage;

        public Mocha(Beverage beverage) {
            this.beverage = beverage;
        }

        @Override
        public String getDescription() {
            return beverage.getDescription() + ", Mocha";
        }

        @Override
        public double cost() {
            return beverage.cost() + 0.2;
        }
    }

    //具体的装饰者
    public static class Whip extends CondimentDecorator {
        Beverage beverage;

        public Whip(Beverage beverage) {
            this.beverage = beverage;
        }

        @Override
        public String getDescription() {
            return beverage.getDescription() + ", Whip";
        }

        @Override
        public double cost() {
            return beverage.cost() + 0.6;
        }
    }

    //具体的装饰者
    public static class Soy extends CondimentDecorator {
        Beverage beverage;

        public Soy(Beverage beverage) {
            this.beverage = beverage;
        }

        @Override
        public String getDescription() {
            return beverage.getDescription() + ", Soy";
        }

        @Override
        public double cost() {
            return beverage.cost() + 0.3;
        }
    }
}

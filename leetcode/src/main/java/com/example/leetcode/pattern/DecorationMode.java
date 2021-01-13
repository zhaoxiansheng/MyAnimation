package com.example.leetcode.pattern;

public class DecorationMode {

    //抽象组件
    public abstract class Beverage {
        String description = "Unknown Beverage";

        public String getDescription() {
            return description;
        }

        public abstract double cost();
    }

    //抽象装饰者
    public abstract class CondimentDecorator extends Beverage {
        public abstract String getDescription();
    }

    public class Espresso extends Beverage {

        public Espresso() {
            description = "Espresso";
        }

        public double cost() {
            return 1.99;
        }
    }

    //具体组件
    public class HouseBlend extends Beverage {
        public HouseBlend() {
            description = "House Blend Coffee";
        }

        public double cost() {
            return 0.99;
        }
    }

    //具体的装饰者
    public class Mocha extends CondimentDecorator {
        Beverage beverage;

        public Mocha(Beverage beverage) {
            this.beverage = beverage;
        }

        public String getDescription() {
            return beverage.getDescription() + ", Mocha";
        }

        public double cost() {
            return beverage.cost() + 0.2;
        }
    }

    //具体的装饰者
    public class Whip extends CondimentDecorator {
        Beverage beverage;

        public Whip(Beverage beverage) {
            this.beverage = beverage;
        }

        public String getDescription() {
            return beverage.getDescription() + ", Whip";
        }

        public double cost() {
            return beverage.cost() + 0.6;
        }
    }

    //具体的装饰者
    public class Soy extends CondimentDecorator {
        Beverage beverage;

        public Soy(Beverage beverage) {
            this.beverage = beverage;
        }

        public String getDescription() {
            return beverage.getDescription() + ", Soy";
        }

        public double cost() {
            return beverage.cost() + 0.3;
        }
    }
}

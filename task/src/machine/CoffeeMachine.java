package machine;
import java.util.*;

public class CoffeeMachine {
    private enum machineState {
        CHOOSE_ACTION, CHOOSE_COFFEE, FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS;
    }

    private enum coffeeType {
        ESPRESSO(1,250, 0, 16, 4),
        LATTE(2,350, 75, 20, 7),
        CAPPUCCINO(3,200, 100, 12, 6);

        private final int index, water, milk, beans, money;

        coffeeType(int index, int water, int milk, int beans, int money) {
            this.index = index;
            this.water = water;
            this.milk = milk;
            this.beans = beans;
            this.money = money;
        }
    }

    private machineState state;
    private int hasWater;
    private int hasMilk;
    private int hasBeans;
    private int hasCups;
    private int hasMoney;

    private int addWater;
    private int addMilk;
    private int addBeans;
    private int addCups;

    public CoffeeMachine(int water, int milk, int beans, int cups, int money) {
        hasWater = water;
        hasMilk = milk;
        hasBeans = beans;
        hasCups = cups;
        hasMoney = money;
        state = machineState.CHOOSE_ACTION;
    }

    private void printState() {
        System.out.println();
        System.out.printf("The coffee machine has:%n%d of water%n%d of milk%n", hasWater, hasMilk);
        System.out.printf("%d of coffee beans%n%d of disposable cups%n$%d of money%n", hasBeans, hasCups, hasMoney);
        System.out.println();
    }

    private String checkSupplies(int water, int milk, int beans) {
        String status;

        if (hasWater / water <= 0) {
            status = "not enough water";
        } else if (milk > 0 && hasMilk / milk <= 0) {
            status = "not enough milk";
        } else if (hasBeans / beans <= 0) {
            status = "not enough beans";
        } else status = "good";

        return status;
    }

    private void makeCoffee(int water, int milk, int beans, int cups, int money) {
        hasWater -= water;
        hasMilk -= milk;
        hasBeans -= beans;
        hasCups--;
        hasMoney += money;
    }

    private void buyCoffee(coffeeType type) {
        int water = type.water;
        int milk = type.milk;
        int beans = type.beans;
        int cups = 1;
        int money = type.money;

        String status = checkSupplies(water, milk, beans);
        if ("good".equals(status)) {
            System.out.println("I have enough resources, making you a coffee!");
            makeCoffee(water, milk, beans, cups, money);
        } else {
            System.out.printf("Sorry, %s%n", status);
        }
    }

    private void fillSupplies(int water, int milk, int beans, int cups) {
        hasWater += water;
        hasMilk += milk;
        hasBeans += beans;
        hasCups += cups;
    }

    private void changeState(machineState state) {
        switch(state) {
            case CHOOSE_ACTION:
                this.state = machineState.CHOOSE_ACTION;
                System.out.println();
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                break;
            case CHOOSE_COFFEE:
                this.state = machineState.CHOOSE_COFFEE;
                System.out.println();
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte" +
                        ", 3 - cappuccino, back - to main menu:");
                break;
            case FILL_WATER:
                this.state = machineState.FILL_WATER;
                System.out.println();
                System.out.println("Write how many ml of water do you want to add:");
                break;
            case FILL_MILK:
                this.state = machineState.FILL_MILK;
                System.out.println();
                System.out.println("Write how many ml of milk do you want to add:");
                break;
            case FILL_BEANS:
                this.state = machineState.FILL_BEANS;
                System.out.println();
                System.out.println("Write how many grams of coffee beans do you want to add:");
                break;
            case FILL_CUPS:
                this.state = machineState.FILL_CUPS;
                System.out.println();
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                break;
            default:
                this.state = machineState.CHOOSE_ACTION;
                break;
        }
    }

    private boolean process(String input) {
        switch(this.state) {
            case CHOOSE_ACTION:
                switch(input) {
                    case "buy":
                        changeState(machineState.CHOOSE_COFFEE);
                        break;
                    case "fill":
                        changeState(machineState.FILL_WATER);
                        break;
                    case "take":
                        System.out.printf("I gave you $%d%n", hasMoney);
                        hasMoney = 0;
                        changeState(machineState.CHOOSE_ACTION);
                        break;
                    case "remaining":
                        printState();
                        changeState(machineState.CHOOSE_ACTION);
                        break;
                    case "exit":
                        return true;
                }
                break;
            case CHOOSE_COFFEE:
                switch(input) {
                    case "1":
                        buyCoffee(coffeeType.ESPRESSO);
                        break;
                    case "2":
                        buyCoffee(coffeeType.LATTE);
                        break;
                    case "3":
                        buyCoffee(coffeeType.CAPPUCCINO);
                        break;
                }
                changeState(machineState.CHOOSE_ACTION);
                break;
            case FILL_WATER:
                addWater = Integer.parseInt(input);
                changeState(machineState.FILL_MILK);
                break;
            case FILL_MILK:
                addMilk = Integer.parseInt(input);
                changeState(machineState.FILL_BEANS);
                break;
            case FILL_BEANS:
                addBeans = Integer.parseInt(input);
                changeState(machineState.FILL_CUPS);
                break;
            case FILL_CUPS:
                addCups = Integer.parseInt(input);
                fillSupplies(addWater, addMilk, addBeans, addCups);
                changeState(machineState.CHOOSE_ACTION);
                break;
            default:
                break;
        }

        return false;
    }

    public static void main(String[] args) {
        CoffeeMachine machine = new CoffeeMachine(400, 540, 120, 9, 550);
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Write action (buy, fill, take, remaining, exit):");

        while (!exit) {
            String input = scan.next();
            exit = machine.process(input);
        }
    }
}
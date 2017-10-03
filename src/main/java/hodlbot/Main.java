package hodlbot;

import hodlbot.bittrex.Bittrex;
import hodlbot.loop.MA2x;
import hodlbot.loop.Ticker;

import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static Bittrex bittrex;

    public static void main(String[] args) throws InterruptedException {

        scanner = new Scanner(System.in);
        bittrex = new Bittrex();
        bittrex.setAuthKeys();

        System.out.println("**************************");
        System.out.println("*******hodlbot v0.1*******");
        System.out.println("**************************");
        System.out.println("1 for manual sell/buyback");
        System.out.println("2 to run 2xMA");
        System.out.println("3 to run pricescan");
        System.out.println("----");

        int choice = scanner.nextInt();

        while (choice != -1) {

            switch (choice) {
                case 1:
                    manualSell();
                    break;
                case 2:
                    runMA2x();
                    break;
                case 3:
                    runPriceScan();
                default:
                    break;
            }

            choice = scanner.nextInt();

        }
    }

    private static void runPriceScan() {
        System.out.println("loop interval (sec): ");
        float interval = scanner.nextFloat();
        Ticker.start(bittrex, interval);
    }

    private static void runMA2x() {

        System.out.println("loop interval (min): ");
        float interval = scanner.nextFloat();
        MA2x.start(bittrex, interval);
    }

    private static void manualSell() throws InterruptedException {

        System.out.print("NEO sell amount.  -1 to stop");
        float sellAmount = scanner.nextFloat();

        while (sellAmount != -1){
        //MarketOrder.sell(bittrex, "BTC-NEO", sellAmount);
            System.out.println("selling: " + sellAmount);

            System.out.print("NEO sell: ");
            sellAmount = scanner.nextFloat();
        }

    }
}

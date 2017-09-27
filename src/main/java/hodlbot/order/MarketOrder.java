package hodlbot.order;

import hodlbot.bittrex.Bittrex;

import java.util.HashMap;
import java.util.List;

// friskel 2017
// faux market order (because they are disabled on bittrex) with limit buyback
// just does a limit sell order with a rate of slightly less than the bid
// re-buys

public class MarketOrder {

    private static String uuid;


    public static String sell(Bittrex wrapper, String pair, float amount) throws InterruptedException {

        //get the current bid
        final String response = wrapper.getTicker(pair);
        Double bid = Double.parseDouble(Bittrex.getMapsFromResponse(response).get(0).get("Bid"));

        //adjust price so it definitely fills
        String sellRate = String.valueOf(bid * 0.98f);

        //print bid
        System.out.println("faux marketsell at bid of: " + String.format("%.8f", bid));

        //sell happens here, response stored in string json
        String json = wrapper.sellLimit(pair, String.valueOf(amount), sellRate);
        Thread.sleep(3000);
        if (json.contains("success")) {
            uuid = Bittrex.getMapsFromResponse(json).get(0).get("uuid");
            System.out.println("success: " + uuid);
            getFreshSellInfo(wrapper, uuid, pair);
        } else { System.out.println("sell failed!  :c"); }

        return uuid;
    }

    public static void getFreshSellInfo(Bittrex wrapper, String uuid, String pair) throws InterruptedException {

        //majick
        String rawResponse = wrapper.getOrder(uuid);
        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        HashMap<String, String> order = responseMapList.get(0);
        Thread.sleep(400);
        boolean openOrder = Boolean.getBoolean(order.get("IsOpen"));
        double actualPrice = Double.valueOf(order.get("PricePerUnit"));
        double qtySold = Double.valueOf(order.get("Quantity"));

        double sellAmount = Double.valueOf(order.get("Price"));
        double fee = Double.valueOf(order.get("CommissionPaid"));

        if(!openOrder){
            System.out.println("sell successful!");
            Thread.sleep(2000);
            System.out.println("actual price: " + String.format("%.8f", actualPrice));
            Thread.sleep(1000);
            System.out.println("amount sold: " + String.format("%.8f", qtySold));
            Thread.sleep(1000);
            System.out.println("btc received: " + String.format("%.8f", sellAmount));
            Thread.sleep(500);
            System.out.println("fee: " + String.format("%.8f", fee));
            Thread.sleep(1000);
            buyback(wrapper, pair, actualPrice, qtySold);
        }
    }

    public static void buyback(Bittrex wrapper, String pair, double sellAmount, double amt) throws InterruptedException {

        double profitRate = .99;
        double hookFee = .005;

        double buybackPrice = sellAmount * (profitRate - hookFee);

        double btc;
        Thread.sleep(1000);
        btc = Double.valueOf(Bittrex.getMapsFromResponse(wrapper.getBalance("BTC")).get(0).get("Balance"));
        Thread.sleep(1000);
        System.out.println("btc to buyback: " + String.format("%.8f", btc));

        System.out.println("buyback price: " + String.format("%.8f", buybackPrice));


        double buybackAmount = amt * 1.0075;

        System.out.println("trying to buy back amt: " + String.format("%.8f", buybackAmount));


        String json = wrapper.buyLimit(pair, String.valueOf(buybackAmount), String.valueOf(buybackPrice));
        Thread.sleep(3000);
        if (json.contains("success")) {
            System.out.println(json);
            getFreshBuyInfo(wrapper, Bittrex.getMapsFromResponse(json).get(0).get("uuid"), pair);
//            uuid = Bittrex.getMapsFromResponse(json).get(0).get("uuid");
//            System.out.println("success: " + uuid);

        } else { System.out.println("buyback failed!  :c"); }

    }

    public static void getFreshBuyInfo(Bittrex wrapper, String uuid, String pair) throws InterruptedException {

        //majick
        String rawResponse = wrapper.getOrder(uuid);
        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        HashMap<String, String> order = responseMapList.get(0);
        Thread.sleep(400);


        HashMap<String, String> onlyMap = responseMapList.get(0);

        System.out.println("Quantity: " + String.format("%.8f", Double.valueOf(onlyMap.get("Quantity"))));
        System.out.println("CommissionReserved: " + String.format("%.8f", Double.valueOf(onlyMap.get("CommissionReserved"))));
        System.out.println("Estimated total: " + String.format("%.8f", Double.valueOf(onlyMap.get("ReserveRemaining"))));
        System.out.println("Limit buy: " + String.format("%.8f", Double.valueOf(onlyMap.get("Limit"))));



        ////////todo:  calc profit after buy order is in books





//        boolean openOrder = Boolean.getBoolean(order.get("IsOpen"));
//        double actualPrice = Double.valueOf(order.get("PricePerUnit"));
//        double qtySold = Double.valueOf(order.get("Quantity"));
//
//        double sellAmount = Double.valueOf(order.get("Price"));
//        double fee = Double.valueOf(order.get("CommissionPaid"));
//
//        if(!openOrder){
//            System.out.println("sell successful!");
//            Thread.sleep(2000);
//            System.out.println("actual price: " + String.format("%.8f", actualPrice));
//            Thread.sleep(1000);
//            System.out.println("amount sold: " + String.format("%.8f", qtySold));
//            Thread.sleep(1000);
//            System.out.println("btc received: " + String.format("%.8f", sellAmount));
//            Thread.sleep(500);
//            System.out.println("fee: " + String.format("%.8f", fee));
//            Thread.sleep(1000);
//            buyback(wrapper, pair, actualPrice, qtySold);
        }
    }




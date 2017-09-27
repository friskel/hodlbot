package hodlbot;

import hodlbot.bittrex.Bittrex;
import hodlbot.order.MarketOrder;

import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Bittrex wrapper = new Bittrex();
        wrapper.setAuthKeys();

        MarketOrder.sell(wrapper, "BTC-NEO", 25f);
//        MarketOrder.getFreshSellInfo(wrapper, "58df7fe5-591d-4b16-aebf-cf37a1696e37", "BTC-XMR");


    }
}

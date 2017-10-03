package hodlbot.loop;

import hodlbot.bittrex.Bittrex;

public class Ticker {


    public static void start(final Bittrex bittrex, final float interval) {




        // start a loop
        new Thread(new Runnable() {
            public void run() {

                int count = 0;
                while (true) {

                    System.out.println("~~~~~~~ ticker period #" + ++count + " start");
                    float bid = Float.valueOf(Bittrex.getMapsFromResponse(bittrex.getTicker("BTC-NEO")).get(0).get("Bid"));


                    float ma1sum = MA2x.getMa1sum();
                    float ma2sum = MA2x.getMa2sum();

                    float dif = (1-(ma2sum/ma1sum));

                    System.out.printf("%s%.8f%n", "bid: ", bid);
                    System.out.printf("%s%.8f%n", "3/50 MA height %: ", dif);
                    if (dif > .01){
                        System.out.println("over 1% sell!");
                    } else {
                        System.out.println("hodl!");
                    }

                    System.out.println("~~~~~~~ ticker period #" + count + " end");
                    System.out.println();

                    try {
                        Thread.sleep((long) interval * 1000);
                    } //

                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

    }
}

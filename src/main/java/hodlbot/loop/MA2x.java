package hodlbot.loop;

import hodlbot.bittrex.Bittrex;

import java.util.LinkedList;

public class MA2x {

    private static LinkedList<Float> ma1 = new LinkedList<>();
    private static LinkedList<Float> ma2 = new LinkedList<>();

    private static int periodMA1 = 3;
    private static int periodMA2 = 50;

    private static float ma1sum;
    private static float ma2sum;




    public static void start(final Bittrex bittrex, final float interval){



        // start a loop
        new Thread(new Runnable()
        {
            public void run()
            {
                int count = 0;
                while(true) {
                    System.out.println("------------ MA period #" + ++count + " start");
                    System.out.println("looping " + interval + " min");

                    getPrice(bittrex);

                    System.out.println("------------ MA period #" + count + " end");
                    System.out.println();

                    try {
                        Thread.sleep((long) interval * 60 * 1000);
                    } //

                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();





    }

    public static void getPrice(Bittrex bittrex) {

        float last = Float.valueOf(Bittrex.getMapsFromResponse(bittrex.getTicker("BTC-NEO")).get(0).get("Last"));

        System.out.printf("%s%.8f%n", "price: ", last);

        ma1sum = addToMA(ma1, periodMA1, last);
        ma2sum = addToMA(ma2, periodMA2, last);

        float higher = ma2sum-ma1sum;
        float percentDif = ma2sum/ma1sum;

        System.out.printf("%s%.8f%s%f%n", "ma1 is ", higher, " higher than ma2. x", (1 - percentDif));

    }



    private static float addToMA(LinkedList<Float> ma, int period, float last) {

        ma.add(last);

        if (ma.size() > period){
            ma.removeFirst();
        }

        float maSum = 0;

        for (float theLast : ma){
            maSum += theLast;
        } maSum /= ma.size();





        System.out.println("MA period: " + period + " MA size: " + ma.size());
        System.out.printf("%s%.8f%n", "ma total: ", maSum);

        return maSum;

    }

    public static float getMa1sum() {
        return ma1sum;
    }

    public static float getMa2sum() {
        return ma2sum;
    }
}

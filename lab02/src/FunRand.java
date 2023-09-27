
import java.util.Random;

public  class FunRand {
    /**
     * Generates random value according to an exponential distribution
     *
     * @param timeMean the mean value
     * @return a random value according to an exponential distribution
     */
    public static double exp(double timeMean) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = -timeMean * Math.log(a);

        return a;
    }

    /**
     * Generates random value according to a uniform distribution
     *
     * @param timeMin the minimum value of random value
     * @param timeMax the maximum value of random value
     * @return a random value according to a uniform distribution
     */
    public static double unif(double timeMin, double timeMax) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = timeMin + a * (timeMax - timeMin);

        return a;
    }

    /**
     * Generates random value according to a normal (Gauss) distribution
     *
     * @param timeMean the mean of random value
     * @param timeDeviation the deviation of random value
     * @return a random value according to a normal (Gauss) distribution
     */
    public static double norm(double timeMean, double timeDeviation) {
        double a;
        Random r = new Random();
        a = timeMean + timeDeviation * r.nextGaussian();

        return a;
    }

}
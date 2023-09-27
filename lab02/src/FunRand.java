
import java.util.Random;

public  class FunRand {
    public static double exp(double timeMean) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = -timeMean * Math.log(a);

        return a;
    }
    public static double unif(double timeMin, double timeMax) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = timeMin + a * (timeMax - timeMin);

        return a;
    }
    public static double norm(double timeMean, double timeDeviation) {
        double a;
        Random r = new Random();
        a = timeMean + timeDeviation * r.nextGaussian();

        return a;
    }

}
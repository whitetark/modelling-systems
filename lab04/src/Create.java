import java.util.List;

public class Create extends Element{
    public Create(double delay, String name) {
        super(delay, name);
    }
    @Override
    public void outAct(double tcurr) {
        super.outAct(tcurr);
        double delay = getDelay();
        totalWorkTime += delay;
        tstate = tcurr + delay;
        next.getNextElement().inAct(tcurr);
    }
    @Override
    public void doStatistics(double delta) {
        meanQueue = meanQueue + queue * delta;
    }
}
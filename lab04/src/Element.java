import java.util.List;

public class Element {
    protected double meanQueue, tstate, delay, totalWorkTime;
    protected int state, queue, maxQueue, failure, served;
    protected NextElements next;

    protected DistributionType distributionType;

    protected String name;

    public Element(String name) {
        this.delay = 0;
        maxQueue = Integer.MAX_VALUE;
        this.name = name;
        tstate = 0;
        next = null;

    }
    public Element(double delay, String name) {
        this.delay = delay;
        maxQueue = Integer.MAX_VALUE;
        this.name = name;
        tstate = 0;
        next = null;

    }
    public Element(double delay, String name, int maxQueue) {
        this.delay = delay;
        this.maxQueue = maxQueue;
        this.name = name;
        tstate = 0;
        next = null;
    }

    public void inAct(double tcurr) {
    }
    public void outAct(double tcurr){
        served++;
    }
    protected void printInfo() {
        System.out.println("Event = " + name + " tnext = " + tstate + " queue: " + queue + " state = " + state);
    }
    protected void printStatistic(double timeModeling){
        System.out.println("Event = " + name + " served = " + served + " failure = "+failure);
    }
    public void printResult(){
        System.out.println(name+ " served = "+ served);
    }

    public void doStatistics(double delta){
    }
    public void setNextElement(NextElements next) {
        this.next = next;
    }

    protected double getDelay() {
        switch (distributionType) {
            case EXPONENTIAL:
                return FunRand.exp(delay);
            case UNIFORM:
                return FunRand.unif(delay+delay*0.1, delay-delay*0.1);
            case NORMAL:
                return FunRand.norm(delay, 0.4);
            default:
                return FunRand.exp(delay);
        }
    }

    public void setDistributionType(DistributionType distributionType) {
        this.distributionType = distributionType;
    }

    protected void setTstate(double tstate) {
        this.tstate = tstate;
    }

    protected void setState(int state) {
        this.state = state;
    }

    public int getQueue() {
        return queue;
    }
}
package hospital;
import java.util.List;

public class Element {
    protected double meanQueue, tstate, delay, delayfrom, delayto, totalWorkTime;
    protected int state, maxQueue, failure, served, k;

    protected ClientType currentClientType;
    protected NextElementsOnClientType next;

    protected List<ClientType> queue;
    protected DistributionType distributionType = DistributionType.EXPONENTIAL;

    protected String name;

    public Element(String name) {
        this.delay = 0;
        maxQueue = 0;
        this.name = name;
        tstate = 0;
        next = null;

    }
    public Element(double delay, String name) {
        this.delay = delay;
        maxQueue = 0;
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
    public void inAct(double tcurr, ClientType clientType) {
    }

    public void outAct(double tcurr){
        served++;
    }

    public void outAct(double tcurr, ClientType clientType){
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
    public void setNextElement(NextElementsOnClientType next) {
        this.next = next;
    }

    protected double getDelay() {
        switch (distributionType) {
            case EXPONENTIAL:
                return FunRand.exp(delay);
            case UNIFORM:
                return FunRand.unif(delayfrom, delayto);
            case NORMAL:
                return FunRand.norm(delay, 0.4);
            case ERLANG:
                return FunRand.erlang(delay, k);
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

    public List<ClientType> getQueue() {
        return queue;
    }

    public void setUniformDistribution(double delayfrom, double delayto) {
        distributionType = DistributionType.UNIFORM;
        this.delayfrom = delayfrom;
        this.delayto = delayto;
    }

    public void setErlangDistribution(double mean, int k) {
        distributionType = DistributionType.ERLANG;
        this.delay = mean;
        this.k = k;
    }
}
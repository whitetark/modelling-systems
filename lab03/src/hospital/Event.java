package hospital;
import java.util.List;

public class Event {
    protected double meanQueue, eventTime, delay, delayFrom, delayTo, totalWorkTime;
    protected int state, maxQueue, failure, served, k;
    protected ClientType currentClientType;
    protected NextEventsOnClientType next;

    protected List<ClientType> queue;
    protected DistributionType distributionType = DistributionType.EXPONENTIAL;

    protected String name;

    public Event(String name) {
        this.delay = 0;
        maxQueue = 0;
        this.name = name;
        eventTime = 0;
        next = null;

    }
    public Event(double delay, String name) {
        this.delay = delay;
        maxQueue = 0;
        this.name = name;
        eventTime = 0;
        next = null;

    }
    public Event(double delay, String name, int maxQueue) {
        this.delay = delay;
        this.maxQueue = maxQueue;
        this.name = name;
        eventTime = 0;
        next = null;
    }

    public void inAct(double currentTime) {
    }
    public void inAct(double currentTime, ClientType clientType) {
    }

    public void outAct(double currentTime){
        served++;
    }

    public void outAct(double currentTime, ClientType clientType){
        served++;
    }
    protected void printInfo() {
        System.out.println("Event = " + name + " nextEventTime = " + eventTime + " queue: " + queue + " state = " + state);
    }
    protected void printStatistic(double timeModeling){
        System.out.println("Event = " + name + " served = " + served + " failure = "+failure);
    }
    public void printResult(){
        System.out.println(name+ " served = "+ served);
    }

    public void doStatistics(double delta){
    }
    public void setNextElement(NextEventsOnClientType next) {
        this.next = next;
    }

    protected double getDelay() {
        switch (distributionType) {
            case EXPONENTIAL:
                return FunRand.exp(delay);
            case UNIFORM:
                return FunRand.unif(delayFrom, delayTo);
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

    protected void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    protected void setState(int state) {
        this.state = state;
    }

    public List<ClientType> getQueue() {
        return queue;
    }

    public void setUniformDistribution(double delayFrom, double delayTo) {
        distributionType = DistributionType.UNIFORM;
        this.delayFrom = delayFrom;
        this.delayTo = delayTo;
    }

    public void setErlangDistribution(double mean, int k) {
        distributionType = DistributionType.ERLANG;
        this.delay = mean;
        this.k = k;
    }
}
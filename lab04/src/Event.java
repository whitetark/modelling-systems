public class Event {
    protected double meanQueue, eventTime, delay, totalWorkTime;
    protected int state, queue, maxQueue, failure, served;
    protected NextEvents next;

    protected DistributionType distributionType;

    protected String name;

    public Event(String name) {
        this.delay = 0;
        maxQueue = Integer.MAX_VALUE;
        this.name = name;
        eventTime = 0;
        next = null;

    }
    public Event(double delay, String name) {
        this.delay = delay;
        maxQueue = Integer.MAX_VALUE;
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
    public void outAct(double currentTime){
        served++;
    }
    protected void printInfo() {
        System.out.println("Event = " + name + " eventTime = " + eventTime + " queue: " + queue + " state = " + state);
    }
    protected void printStatistic(double timeModeling){
        System.out.println("Event = " + name + " served = " + served + " failure = "+failure);
    }
    public void printResult(){
        System.out.println(name+ " served = "+ served);
    }

    public void doStatistics(double delta){
    }
    public void setNextEvent(NextEvents next) {
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

    protected void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    protected void setState(int state) {
        this.state = state;
    }

    public int getQueue() {
        return queue;
    }
}
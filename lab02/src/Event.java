import java.util.List;

public class Event {
    protected double meanQueue, tstate, delay, meanLoad, maxQueue;
    protected int state, queue, failure, served;
    protected Event next;
    protected String name;

    public Event(double delay, String name) {
        this.delay = delay;
        maxQueue = 0;
        this.name = name;
        tstate = 0;
        next = null;
    }
    public Event(double delay, String name, double maxQueue) {
        this.delay = delay;
        this.maxQueue = maxQueue;
        this.name = name;
        tstate = 0;
        next = null;
    }

    public void inAct(double tcurr) {
    }
    public void outAct(double tcurr, List<Event> events){
        served++;
    }
    protected void printInfo() {
        System.out.println("Event = " + name + " tnext = " + tstate + " queue: " + queue);
    }
    protected void printStatistic(){
        System.out.println("Event = " + name + " served = " + served + " failure = "+failure);
    }
    public void printResult(double tcurr){
        System.out.println(name+ " served = "+ served);
    }

    public void doStatistics(double delta){
    }
    public void setNextElement(Event nextElement) {
        this.next = nextElement;
    }
    protected double getDelay() {
        return FunRand.exp(delay);
    }
}

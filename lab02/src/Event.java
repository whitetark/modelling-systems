import java.util.List;

public class Event {
    protected double meanQueue, eventTime, delay, meanLoad, maxQueue;
    protected int queue, failure, served;
    protected Event next;
    protected String name;

    public Event(double delay, String name) {
        this.delay = delay;
        maxQueue = 0;
        this.name = name;
        eventTime = 0;
        next = null;
    }
    public Event(double delay, String name, double maxQueue) {
        this.delay = delay;
        this.maxQueue = maxQueue;
        this.name = name;
        eventTime = 0;
        next = null;
    }

    public void inAct(double currentTime) {
    }
    public void outAct(double currentTime, List<Event> events){
        served++;
    }
    protected void printInfo() {
        System.out.println("Event = " + name + " nextEventTime = " + eventTime + " queue: " + queue);
    }
    protected void printStatistic(){
        System.out.println("Event = " + name + " served = " + served + " failure = " + failure);
    }
    public void printResult(double currentTime){
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

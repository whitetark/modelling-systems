package bank;

public class NextEvent {
    protected Event event;
    protected double probability;
    protected int priority;

    public NextEvent(Event event, double probability, int priority) {
        this.event = event;
        this.probability = probability;
        this.priority = priority;
    }

    public NextEvent(Event event, int priority) {
        this.event = event;
        this.priority = priority;
    }

    public NextEvent(Event event, double probability) {
        this.event = event;
        this.probability = probability;
    }
}

import java.util.*;
public class NextEvents {
    private List<NextEvent> nextEvents;
    private NextEventsType type;

    public NextEvents(List<NextEvent> nextEvents, NextEventsType type) {
        if(type == NextEventsType.PROBABILITY) {
            double sum = 0;
            for (NextEvent nextEvent : nextEvents) {
                sum += nextEvent.probability;
            }
            if (sum != 0 && sum != 1) {
                throw new IllegalArgumentException("Sum of probabilities must be 1");
            }
        }
        this.nextEvents = nextEvents;
        this.type = type;
    }

    public Event getNextEvent() {
        switch (type) {
            case PRIORITY:
                return getPriorityEvent(nextEvents);
            case PROBABILITY:
                return getProbabilityEvent();
            case PRIORITY_WITH_QUEUE:
                return getPriorityWithQueueEvent();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    private Event getPriorityEvent(List<NextEvent> nextEvents) {
        Event event = null;
        int maxPriority = Integer.MIN_VALUE;
        for (NextEvent nextEvent : nextEvents) {
            if (nextEvent.priority > maxPriority) {
                maxPriority = nextEvent.priority;
                event = nextEvent.event;
            }
        }
        return event;
    }

    private Event getPriorityWithQueueEvent() {
        Event event = null;
        List<NextEvent> lowestQueueElements = getEventsWithLowestQueue();
        event = getPriorityEvent(lowestQueueElements);
        return event;
    }

    private List<NextEvent> getEventsWithLowestQueue() {
        List<NextEvent> lowestQueueElements = new ArrayList<>();
        int lowestQueue = Integer.MAX_VALUE;

        for (NextEvent nextEvent : nextEvents) {
            if (nextEvent.event.getQueue() < lowestQueue) {
                lowestQueue = nextEvent.event.getQueue();
                lowestQueueElements.clear(); // Clear the previous list as we found a lower queue
                lowestQueueElements.add(nextEvent);
            } else if (nextEvent.event.getQueue() == lowestQueue) {
                lowestQueueElements.add(nextEvent);
            }
        }

        return lowestQueueElements;
    }

    private Event getProbabilityEvent() {
        double random = Math.random();
        double sum = 0;
        for (NextEvent nextEvent : nextEvents) {
            sum += nextEvent.probability;
            if (random < sum) {
                return nextEvent.event;
            }
        }
        return null;
    }
}

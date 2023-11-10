package hospital;
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

    public Event getNextElement() {
        switch (type) {
            case PRIORITY:
                return getPriorityElement(nextEvents);
            case PROBABILITY:
                return getProbabilityElement();
            case PRIORITY_WITH_QUEUE:
                return getPriorityWithQueueElement();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    private Event getPriorityElement(List<NextEvent> nextEvents) {
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

    private Event getPriorityWithQueueElement() {
        Event event = null;
        List<NextEvent> lowestQueueElements = getElementsWithLowestQueue();
        event = getPriorityElement(lowestQueueElements);
        return event;
    }

    private List<NextEvent> getElementsWithLowestQueue() {
        List<NextEvent> lowestQueueElements = new ArrayList<>();
        int lowestQueue = Integer.MAX_VALUE;

        for (NextEvent nextEvent : nextEvents) {
            if (nextEvent.event.getQueue().size() < lowestQueue) {
                lowestQueue = nextEvent.event.getQueue().size();
                lowestQueueElements.clear(); // Clear the previous list as we found a lower queue
                lowestQueueElements.add(nextEvent);
            } else if (nextEvent.event.getQueue().size() == lowestQueue) {
                lowestQueueElements.add(nextEvent);
            }
        }

        return lowestQueueElements;
    }

    private Event getProbabilityElement() {
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

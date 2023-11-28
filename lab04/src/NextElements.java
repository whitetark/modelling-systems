import java.util.*;
public class NextElements {
    private List<NextElement> nextElements;
    private NextElementsType type;

    public NextElements(List<NextElement> nextElements, NextElementsType type) {
        if(type == NextElementsType.PROBABILITY) {
            double sum = 0;
            for (NextElement nextElement : nextElements) {
                sum += nextElement.probability;
            }
            sum = Math.round(sum * 100.0) / 100.0;
            if (sum != 0 && sum != 1) {
                throw new IllegalArgumentException("Sum of probabilities must be 1");
            }
        }
        this.nextElements = nextElements;
        this.type = type;
    }

    public Element getNextElement() {
        switch (type) {
            case PRIORITY:
                return getPriorityElement(nextElements);
            case PROBABILITY:
                return getProbabilityElement();
            case PRIORITY_WITH_QUEUE:
                return getPriorityWithQueueElement();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    private Element getPriorityElement(List<NextElement> nextElements) {
        Element element = null;
        int maxPriority = Integer.MIN_VALUE;
        for (NextElement nextElement : nextElements) {
            if (nextElement.priority > maxPriority) {
                maxPriority = nextElement.priority;
                element = nextElement.element;
            }
        }
        return element;
    }

    private Element getPriorityWithQueueElement() {
        Element element = null;
        List<NextElement> lowestQueueElements = getElementsWithLowestQueue();
        element = getPriorityElement(lowestQueueElements);
        return element;
    }

    private List<NextElement> getElementsWithLowestQueue() {
        List<NextElement> lowestQueueElements = new ArrayList<>();
        int lowestQueue = Integer.MAX_VALUE;

        for (NextElement nextElement : nextElements) {
            if (nextElement.element.getQueue() < lowestQueue) {
                lowestQueue = nextElement.element.getQueue();
                lowestQueueElements.clear(); // Clear the previous list as we found a lower queue
                lowestQueueElements.add(nextElement);
            } else if (nextElement.element.getQueue() == lowestQueue) {
                lowestQueueElements.add(nextElement);
            }
        }

        return lowestQueueElements;
    }

    private Element getProbabilityElement() {
        double random = Math.random();
        double sum = 0;
        for (NextElement nextElement : nextElements) {
            sum += nextElement.probability;
            if (random < sum) {
                return nextElement.element;
            }
        }
        return null;
    }
}

import java.util.*;

public class Process extends Event {
    public List<Integer> workerStates = new ArrayList<>();
    private List<Double> workerTimes = new ArrayList<>();
    public Process(double delay, String name, Double maxQueue, int countOfWorkers) {
        super(delay, name, maxQueue);
        eventTime = Double.MAX_VALUE;
        for (int i = 0; i < countOfWorkers; i++) {
            workerStates.add(0);
            workerTimes.add(Double.MAX_VALUE);
        }
    }

    @Override
    public void inAct(double currentTime) {
        int index = workerStates.indexOf(0);
        if (index != -1) {
            workerStates.set(index, 1);
            workerTimes.set(index, currentTime + getDelay());
            eventTime = Collections.min(workerTimes);
        } else {
            if (queue < maxQueue) {
                queue += 1;
            } else {
                failure++;
            }
        }
    }

    public void setNextEvent(List <Event> events) {
        int processEventsCount = events.size() - 1;
        double step = 1.0 / processEventsCount;
        double randomNumber = Math.random();
        for (int i = 0; i < processEventsCount; i++) {
            if (randomNumber >= i * step && randomNumber < (i + 1) * step) {
                Event nextEvent = events.get(i + 1);
                if(!Objects.equals(nextEvent.name, this.name)) {
                    next = nextEvent;
                }
                break;
            }
        }
    }

    @Override
    public void outAct(double currentTime, List <Event> events) {
        super.outAct(currentTime, events);
        int index = workerTimes.indexOf(currentTime);
        workerStates.set(index, 0);
        workerTimes.set(index, Double.MAX_VALUE);
        eventTime = Collections.min(workerTimes);
        if (queue > 0) {
            queue -= 1;
            workerStates.set(index, 1);
            workerTimes.set(index, currentTime + getDelay());
            eventTime = Collections.min(workerTimes);
        }
        setNextEvent(events);
        if (next != null) {
            next.inAct(currentTime);
        }
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue += queue * delta;
        for(int state : workerStates){
            meanLoad += state * delta;
        }
    }

    @Override
    public void printResult(double currentTime) {
        super.printResult(currentTime);
        System.out.println("Failure = " + this.failure);
        System.out.println("Average Length of Queue = " + meanQueue / currentTime);
        System.out.println("Failure Probability = " + failure / ((double) served + failure));
        System.out.println("Average Load = " + (meanLoad/workerStates.size())/currentTime);
    }

}

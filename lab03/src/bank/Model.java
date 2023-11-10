package bank;

import java.util.List;
import java.util.ArrayList;

public class Model {
    private double nextEventTime;
    private double currentTime;
    int changeQueue = 0;
    private List <Event> events = new ArrayList<>();
    private List <MultiTaskProcessor> processes;
    public Model(Create create, List<MultiTaskProcessor> process) {
        nextEventTime=0.0;
        currentTime = nextEventTime;
        events.add(create);
        for (Event event : process) {
            events.add(event);
        }
        processes = process;
    }

    /**
     * Метод моделювання
     * @param timeModeling - час моделювання в умовних одиницях часу
     */
    public void simulate(double timeModeling){
        while(currentTime<timeModeling) {
            nextEventTime = Double.MAX_VALUE;       // Час наступної події
            Event nextEvent = null;         // Подія, яка станеться найближчою

            for (Event event : events) {
                if (event.eventTime < nextEventTime) {
                    nextEventTime = event.eventTime;
                    nextEvent = event;
                }
            }
            //System.out.println("\nIt's time for element in " +nextElement.name +", time = " + tnext);
            for (Event e : events) {
                e.doStatistics(nextEventTime - currentTime);
            }
            currentTime = nextEventTime;

            for (Event event : events) {
                if(event.eventTime == currentTime) {
                    event.outAct(currentTime);
                }
            }
            tryToSwitchQueue();
            printInfo();
        }
        printResult(timeModeling);
    }
    public void printInfo() {
        for (Event e : events) {
            if(e.state == 1)
                e.printInfo();
        }
    }
    public void printResult( double timeModeling) {
        System.out.println("\n-------------RESULTS-------------");
        int totalClients = 0;
        for (Event e : events) {
            e.printResult();
            if (e instanceof MultiTaskProcessor) {
                totalClients += e.served;
                MultiTaskProcessor p = (MultiTaskProcessor) e;
                System.out.println("mean length of queue = " + p.meanQueue / currentTime + "\nfailure probability = " +
                        p.failure / ((double) p.served + p.failure) + "\nawg load time = " + p.getTotalWorkTime() / p.getProcessCount() / timeModeling);
                System.out.println("Average Exit Interval = " + p.totalExitTime / (p.totalCustomersExited - 1));
                System.out.println("Average Time in system = " + (p.totalEnterTimeEnd - p.totalEnterTimeStart) / p.served);
                for (Process process : p.getProcesses()) {
                    System.out.println("load time in " + process.name + " = " + process.totalWorkTime / timeModeling);
                }
            }
            System.out.println();
        }
        System.out.println("mean clients = " + (double) totalClients / (double) timeModeling);
        System.out.println("change queue = " + changeQueue);


    }

    // method only for bank task
    public void tryToSwitchQueue() {
        int minQueue = Integer.MAX_VALUE;
        int maxQueue = 0;
        MultiTaskProcessor minQueueElement = null;
        MultiTaskProcessor maxQueueElement = null;
        for (MultiTaskProcessor element : processes) {
            if (element.queue < minQueue) {
                minQueue = element.queue;
                minQueueElement = element;
            }
            if (element.queue > maxQueue) {
                maxQueue = element.queue;
                maxQueueElement = element;
            }
        }
        double randValue = Math.random();
        if (minQueueElement != null && maxQueueElement != null) {
            if(maxQueueElement.queue - minQueueElement.queue >= 2 && randValue < 0.5) {
                minQueueElement.queue += 1;
                maxQueueElement.queue -= 1;
                changeQueue++;
            }
        }
    }
}
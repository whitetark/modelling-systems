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
            //System.out.println("\nIt's time for event in " +nextEvent.name +", time = " + nextEventTime);
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
                System.out.println("Average Length of Queue = " + p.meanQueue / currentTime);
                System.out.println("Average Load Time = " + p.getTotalWorkTime() / p.processes.size() / timeModeling);
                System.out.println("Average Client Exit Interval = " + p.totalExitTime / (p.totalCustomersExited - 1));
                System.out.println("Average Time in Bank = " + (p.totalEnterTimeEnd - p.totalEnterTimeStart) / p.served);
                if(processes.size() > 2){
                    for (Process process : p.processes) {
                        System.out.println("Load time in " + process.name + " = " + process.totalWorkTime / timeModeling);
                    }
                }
            }
            System.out.println();
        }
        System.out.println("Average Num Of Clients = " + (double) totalClients / timeModeling);
        System.out.println("Num of Changing Queue = " + changeQueue);
    }

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
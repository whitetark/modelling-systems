package hospital;

import java.util.List;
import java.util.ArrayList;

public class Model {
    private double nextEventTime;
    private double currentTime;
    private List <Event> events = new ArrayList<>();
    public static List<Double> timeIn = new ArrayList<>();
    public static List<Double> timeOut = new ArrayList<>();

    public Model(Create create, List<MultiTaskProcessor> process) {
        nextEventTime=0.0;
        currentTime = nextEventTime;
        events.add(create);
        events.addAll(process);
    }

    /**
     * Метод моделювання
     * @param timeModeling - час моделювання в умовних одиницях часу
     */
    public void simulate(double timeModeling){
        while(currentTime < timeModeling) {
            nextEventTime = Double.MAX_VALUE;       // Час наступної події
            Event nextEvent = null;         // Подія, яка станеться найближчою

            for (Event event : events) {
                if (event.eventTime < nextEventTime) {
                    nextEventTime = event.eventTime;
                    nextEvent = event;
                }
            }
//            System.out.println("\nIt's time for element in " +
//                    nextEvent.name +
//                    ", time = " + nextEventTime);
            for (Event e : events) {
                e.doStatistics(nextEventTime - currentTime);
            }
            currentTime = nextEventTime;

            for (Event event : events) {
                if(event.eventTime == currentTime) {
                    event.outAct(currentTime, event.currentClientType);
                }
            }
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
        for (Event e : events) {
            e.printResult();
            if (e instanceof MultiTaskProcessor) {
                MultiTaskProcessor p = (MultiTaskProcessor) e;
                System.out.println("Average Length of Queue = " + p.meanQueue / currentTime);
                System.out.println("Failure Probability = " + p.failure / ((double) p.served + p.failure));
                System.out.println("Average Load Time = " + p.getTotalWorkTime() / p.processes.size() / timeModeling);
                for (Process process : p.processes) {
                    System.out.println("Load Time in " + process.name + " = " + process.totalWorkTime / timeModeling);
                }
            }
            System.out.println();
        }
        double sumTimeIn = 0;
        double sumTimeOut = 0;
        for (int i = 0; i < timeOut.size(); i++) {
            sumTimeIn += timeIn.get(i);
        }
        for (Double time : timeOut) {
            sumTimeOut += time;
        }

        System.out.println("Average Time in System = " + (sumTimeOut - sumTimeIn) / timeIn.size());
        double intervalTimeIn = 0;
        for (int i = 0; i < timeIn.size() - 1; i++) {
            intervalTimeIn += timeIn.get(i + 1) - timeIn.get(i);
        }
        intervalTimeIn /= timeIn.size() - 1;
        System.out.println("Interval Patient Arrival = " + intervalTimeIn);
    }
}
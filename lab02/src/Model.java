import java.util.List;
import java.util.ArrayList;

public class Model {
    private double nextEventTime;
    private double currentTime;
    private List <Event> events = new ArrayList<>();

    public Model(Event create, List<Event> process) {
        nextEventTime = 0.0;
        currentTime = nextEventTime;
        events.add(create);
        for (Event event : process) {
            events.add(event);
        }
    }

    public void simulate(double timeModeling){
        while(currentTime < timeModeling) {
            nextEventTime = Double.MAX_VALUE;
            Event nextEvent = null;

            for (Event event : events) {
                if (event.eventTime < nextEventTime) {
                    nextEventTime = event.eventTime;
                    nextEvent = event;
                }
            }
            //System.out.println("\nEvent " + nextEvent.name + ", time = " + nextEventTime);
            for (Event e : events) {
                e.doStatistics(nextEventTime - currentTime);
                //e.printStatistic();
            }
            currentTime = nextEventTime;

            for (Event event : events) {
                if(event.eventTime == currentTime) {
                    event.outAct(currentTime, events);
                }
            }
            //printInfo();
        }
        printResult(currentTime);
    }
    public void printInfo() {
        for (Event e : events) {
            e.printInfo();
        }
    }
    public void printResult(double currentTime) {
        System.out.println("\n------RESULTS------");
        for (Event e : events) {
            e.printResult(currentTime);
            System.out.println();
        }
    }
}
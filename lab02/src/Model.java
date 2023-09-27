import java.util.List;
import java.util.ArrayList;

public class Model {
    private double tnext;
    private double tcurr;
    private List <Event> events = new ArrayList<>();

    public Model(Event create, List<Event> process) {
        tnext=0.0;
        tcurr = tnext;
        events.add(create);
        for (Event event : process) {
            events.add(event);
        }
    }

    public void simulate(double timeModeling){
        while(tcurr<timeModeling) {
            tnext = Double.MAX_VALUE;
            Event nextEvent = null;

            for (Event event : events) {
                if (event.tstate < tnext) {
                    tnext = event.tstate;
                    nextEvent = event;
                }
            }
            //System.out.println("\nEvent " + nextEvent.name + ", time = " + tnext);
            for (Event e : events) {
                e.doStatistics(tnext - tcurr);
                //e.printStatistic();
            }
            tcurr = tnext;

            for (Event event : events) {
                if(event.tstate == tcurr) {
                    event.outAct(tcurr, events);
                }
            }
            //printInfo();
        }
        printResult(tcurr);
    }
    public void printInfo() {
        for (Event e : events) {
            e.printInfo();
        }
    }
    public void printResult(double tcurr) {
        System.out.println("\n------RESULTS------");
        for (Event e : events) {
            e.printResult(tcurr);
            System.out.println();
        }
    }
}
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
            tnext = Double.MAX_VALUE;       // Час наступної події
            Event nextEvent = null;         // Подія, яка станеться найближчою

            for (Event event : events) {
                if (event.tstate < tnext) {
                    tnext = event.tstate;
                    nextEvent = event;
                }
            }
            //System.out.println("\nIt's time for event in " + nextEvent.name + ", time = " + tnext);
            for (Event e : events) {
                e.doStatistics(tnext - tcurr);
                //e.printStatistic();
            }
            tcurr = tnext;      // перехід в момент tnext

            for (Event event : events) {
                if(event.tstate == tcurr) {
                    event.outAct(tcurr, events);
                }
            }
            //printInfo();
        }
        //printResult(tcurr);
    }
    public void printInfo() {
        for (Event e : events) {
            if(e.state == 1)
                e.printInfo();
        }
    }
    public void printResult(double tcurr) {
        System.out.println("\n-------------RESULTS-------------");
        for (Event e : events) {
            e.printResult(tcurr);
//            if (e instanceof Process) {
//                Process p = (Process) e;
//                System.out.println("Average Length of Queue = " + p.meanQueue / tcurr + "\nFailure Probability = " + p.failure / ((double) p.served + p.failure) +"\nAverage Load = " + p.meanLoad/tcurr);
//            }
            System.out.println();
        }
    }
}
import java.util.List;
import java.util.ArrayList;

public class Model {
    private double tnext;
    private double tcurr;
    int changeQueue = 0;
    private List <Element> elements = new ArrayList<>();
    private List <MultiTaskProcessor> processes;
    public Model(Create create, List<MultiTaskProcessor> process) {
        tnext=0.0;
        tcurr = tnext;
        elements.add(create);
        for (Element element : process) {
            elements.add(element);
        }
        processes = process;
    }

    /**
     * Метод моделювання
     * @param timeModeling - час моделювання в умовних одиницях часу
     */
    public void simulate(double timeModeling){
        while(tcurr<timeModeling) {
            tnext = Double.MAX_VALUE;       // Час наступної події
            Element nextElement = null;         // Подія, яка станеться найближчою

            for (Element element : elements) {
                if (element.tstate < tnext) {
                    tnext = element.tstate;
                    nextElement = element;
                }
            }
            //System.out.println("\nIt's time for element in " +
            //        nextElement.name +
            //        ", time = " + tnext);
            for (Element e : elements) {
                e.doStatistics(tnext - tcurr);
            }
            tcurr = tnext;

            for (Element element : elements) {
                if(element.tstate == tcurr) {
                    element.outAct(tcurr);
                }
            }
            tryToSwitchQueue();
            //printInfo();
        }
        //printResult(timeModeling);
    }
    public void printInfo() {
        for (Element e : elements) {
            if(e.state == 1)
                e.printInfo();
        }
    }
    public void printResult( double timeModeling) {
        System.out.println("\n-------------RESULTS-------------");
        int totalClients = 0;
        for (Element e : elements) {
            e.printResult();
            if (e instanceof MultiTaskProcessor) {
                totalClients += ((MultiTaskProcessor) e).served;
                MultiTaskProcessor p = (MultiTaskProcessor) e;
                System.out.println("mean length of queue = " +
                        p.meanQueue / tcurr
                        + "\nfailure probability = " +
                        p.failure / ((double) p.served + p.failure) +
                        "\nawg load time = " + p.getTotalWorkTime() / p.getProucessCount() / timeModeling);
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
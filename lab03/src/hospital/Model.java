package hospital;

import java.util.List;
import java.util.ArrayList;

public class Model {
    private double tnext;
    private double tcurr;
    private List <Element> elements = new ArrayList<>();
    public static List<Double> timeIn = new ArrayList<>();
    public static List<Double> timeOut = new ArrayList<>();

    public Model(Create create, List<MultiTaskProcessor> process) {
        tnext=0.0;
        tcurr = tnext;
        elements.add(create);
        elements.addAll(process);
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
            System.out.println("\nIt's time for element in " +
                    nextElement.name +
                    ", time = " + tnext);
            for (Element e : elements) {
                e.doStatistics(tnext - tcurr);
            }
            tcurr = tnext;

            for (Element element : elements) {
                if(element.tstate == tcurr) {
                    element.outAct(tcurr, element.currentClientType);
                }
            }
            printInfo();
        }
        printResult(timeModeling);
    }
    public void printInfo() {
        for (Element e : elements) {
            if(e.state == 1)
                e.printInfo();
        }
    }
    public void printResult( double timeModeling) {
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : elements) {
            e.printResult();
            if (e instanceof MultiTaskProcessor) {
                MultiTaskProcessor p = (MultiTaskProcessor) e;
                System.out.println("mean length of queue = " +
                        p.meanQueue / tcurr
                        + "\nfailure probability = " +
                        p.failure / ((double) p.served + p.failure) +
                        "\nawg load time = " + p.getTotalWorkTime() / p.getProucessCount() / timeModeling);
                for (Process process : p.getProcesses()) {
                    System.out.println("load time in " + process.name + " = " + process.totalWorkTime / timeModeling);
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

        System.out.println("mean time in system = " + (sumTimeOut - sumTimeIn) / timeIn.size());
        double intervalTimeIn = 0;
        for (int i = 0; i < timeIn.size() - 1; i++) {
            intervalTimeIn += timeIn.get(i + 1) - timeIn.get(i);
        }
        intervalTimeIn /= timeIn.size() - 1;
        System.out.println("interval patient arrival = " + intervalTimeIn);
    }
}
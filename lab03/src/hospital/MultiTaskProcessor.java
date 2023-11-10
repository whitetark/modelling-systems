package hospital;

import java.util.*;

public class MultiTaskProcessor extends Element {
    private List<Process> processes;
    protected int totalCustomersExited = 0;
    protected double totalExitTime = 0.0;
    protected double lastExitTime = 0.0; // Останній час виходу клієнта
    protected double totalEnterTimeStart = 0.0;
    protected double totalEnterTimeEnd = 0.0;
    protected int maxQueue;

    protected boolean isDoctor = false;

    protected List<ClientType> queue = new ArrayList<>();
    public MultiTaskProcessor(List<Process> processes, String name) {
        super(name);
        this.processes = processes;
        setTState();
    }
    public MultiTaskProcessor(List<Process> processes, String name, int maxQueue) {
        super(name);
        this.processes = processes;
        this.maxQueue = maxQueue;
        setTState();
    }


    @Override
    public void inAct(double tcurr, ClientType clientType) {
        this.currentClientType = clientType;
        inAct(tcurr);
    }
    @Override
    public void inAct(double tcurr) {
        super.inAct(tcurr);
        Process process = getFreeProcess();
        if (process != null) {
            if(isDoctor){
                double delay = getDelayOnType(currentClientType);
                process.delay = delay;
            }
            totalEnterTimeStart += tcurr;
            process.currentClientType = currentClientType;
            process.outAct(tcurr);
            setTState();
        } else {
            if(this.queue.size() < this.maxQueue) {
                this.queue.add(currentClientType);
            }
            else {
                this.failure++;
            }
        }
    }

    @Override
    public void outAct(double tcurr, ClientType clientType) {
        this.currentClientType = clientType;
        outAct(tcurr);
    }
    @Override
    public void outAct(double tcurr) {
        super.outAct(tcurr);
        Process process = getThisProcess(tcurr);
        if (process != null) {
            process.setState(0);
            process.setTstate(Double.MAX_VALUE);
            setTState();
            if (this.queue.size() > 0) {
                if(isDoctor) {
                    int id = this.queue.indexOf(ClientType.FIRST);
                    if (id == -1) {
                        process.currentClientType = this.queue.get(0);
                        process.outAct(tcurr);
                        this.queue.remove(0);
                    } else {
                        process.currentClientType = this.queue.get(id);
                        process.outAct(tcurr);
                        this.queue.remove(id);
                    }
                }
                else {
                    process.currentClientType = this.queue.get(0);
                    process.outAct(tcurr);
                    this.queue.remove(0);
                }
                setTState();
            }

            double exitTime = tcurr; // Час виходу клієнта
            totalCustomersExited++;
            totalExitTime += exitTime - lastExitTime; // Різниця між поточним та попереднім часом виходу
            lastExitTime = exitTime; // Оновлення останнього часу виходу
            totalEnterTimeEnd += tcurr;
        }
    }


    private Process getFreeProcess() {
        for (Process process : processes) {
            if (process.state == 0) {
                return process;
            }
        }
        return null;
    }

    private Process getThisProcess(double tcurr) {
        for (Process process : processes) {
            if (process.tstate == tcurr) {
                return process;
            }
        }
        return null;
    }

    private void setTState() {
        this.tstate = Collections.min(processes.stream().map(process -> process.tstate).toList());
    }

    @Override
    public void setNextElement(NextElementsOnClientType element) {
        for (Process process : processes) {
            process.setNextElement(element);
        }
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = meanQueue + queue.size() * delta;
    }

    @Override
    public void printResult() {
        int totalServed = 0;
        for (Process process : processes) {
            System.out.println(process.name+ " served = "+ process.served);
            totalServed += process.served;
        }
        System.out.println(name+ " served = "+ totalServed);
        System.out.println("failure = " + this.failure);
    }

    public double getTotalWorkTime() {
        double totalWorkTime = 0;
        for (Process process : processes) {
            totalWorkTime += process.totalWorkTime;
        }
        return  totalWorkTime;
    }

    public int getProucessCount() {
        return processes.size();
    }

    public List<Process> getProcesses() {
        return processes;
    }

    @Override
    public List<ClientType> getQueue() {
        return queue;
    }

    private double getDelayOnType(ClientType clientType){
        switch (clientType){
            case FIRST:
                return 15;
            case SECOND:
                return 40;
            case THIRD:
                return 30;
            default:
                return 0.0;
        }
    }
}

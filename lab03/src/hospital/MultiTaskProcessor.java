package hospital;

import java.util.*;

public class MultiTaskProcessor extends Event {
    public List<Process> processes;
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
        setEventTime();
    }
    public MultiTaskProcessor(List<Process> processes, String name, int maxQueue) {
        super(name);
        this.processes = processes;
        this.maxQueue = maxQueue;
        setEventTime();
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
            setEventTime();
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
    public void outAct(double currentTime, ClientType clientType) {
        this.currentClientType = clientType;
        outAct(currentTime);
    }
    @Override
    public void outAct(double currentTime) {
        super.outAct(currentTime);
        Process process = getThisProcess(currentTime);
        if (process != null) {
            process.setState(0);
            process.setEventTime(Double.MAX_VALUE);
            setEventTime();
            if (this.queue.size() > 0) {
                if(isDoctor) {
                    int id = this.queue.indexOf(ClientType.FIRST);
                    if (id == -1) {
                        process.currentClientType = this.queue.get(0);
                        process.outAct(currentTime);
                        this.queue.remove(0);
                    } else {
                        process.currentClientType = this.queue.get(id);
                        process.outAct(currentTime);
                        this.queue.remove(id);
                    }
                }
                else {
                    process.currentClientType = this.queue.get(0);
                    process.outAct(currentTime);
                    this.queue.remove(0);
                }
                setEventTime();
            }

            double exitTime = currentTime; // Час виходу клієнта
            totalCustomersExited++;
            totalExitTime += exitTime - lastExitTime; // Різниця між поточним та попереднім часом виходу
            lastExitTime = exitTime; // Оновлення останнього часу виходу
            totalEnterTimeEnd += currentTime;
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

    private Process getThisProcess(double currentTime) {
        for (Process process : processes) {
            if (process.eventTime == currentTime) {
                return process;
            }
        }
        return null;
    }

    private void setEventTime() {
        this.eventTime = Collections.min(processes.stream().map(process -> process.eventTime).toList());
    }

    @Override
    public void setNextElement(NextEventsOnClientType element) {
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
            System.out.println(process.name+ " [served = "+ process.served + " failure = "+ process.failure+"]");
            totalServed += process.served;
        }
        if(processes.size() > 1){
            System.out.println(name+ " [served = "+ totalServed + " failure = " + this.failure+"]");
        }
    }

    public double getTotalWorkTime() {
        double totalWorkTime = 0;
        for (Process process : processes) {
            totalWorkTime += process.totalWorkTime;
        }
        return  totalWorkTime;
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

import java.util.*;

public class MultiTaskProcessor extends Event {
    private List<Process> processes;

    protected int totalCustomersExited = 0;
    protected double totalExitTime = 0.0;
    protected double lastExitTime = 0.0; // Останній час виходу клієнта
    protected double totalEnterTimeStart = 0.0;
    protected double totalEnterTimeEnd = 0.0;
    protected int queue, maxQueue;
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
    public void inAct(double currentTime) {
        super.inAct(currentTime);
        Process process = getFreeProcess();
        if (process != null) {
            totalEnterTimeStart += currentTime;
            process.outAct(currentTime);
            setTState();
        } else {
            if(this.queue < this.maxQueue) {
                this.queue += 1;
            }
            else {
                this.failure++;
            }
        }
    }
    @Override
    public void outAct(double currentTime) {
        super.outAct(currentTime);
        Process process = getThisProcess(currentTime);
        if (process != null) {
            process.setState(0);
            process.setEventTime(Double.MAX_VALUE);
            setTState();
            if (this.queue > 0) {
                process.outAct(currentTime);
                this.queue -= 1;
                setTState();
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

    private void setTState() {
        this.eventTime = Collections.min(processes.stream().map(process -> process.eventTime).toList());
    }

    @Override
    public void setNextEvent(NextEvents element) {
        for (Process process : processes) {
            process.setNextEvent(element);
        }
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = meanQueue + queue * delta;
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

    public int getProcessCount() {
        return processes.size();
    }

    public List<Process> getProcesses() {
        return processes;
    }

    @Override
    public int getQueue() {
        return queue;
    }

    public Process getFirstProcess(){
        return processes.get(0);
    }
}

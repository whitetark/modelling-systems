package bank;

public class Process extends Event {
    public Process(double delay, String name, DistributionType distributionType) {
        super(delay, name, distributionType);
        this.eventTime = Double.MAX_VALUE;
    }

    @Override
    public void outAct(double currentTime) {
        super.outAct(currentTime);
            this.state = 1;
            double delay = getDelay();
            this.eventTime = currentTime + delay;
            totalWorkTime += delay;
            if (this.next != null) {
                this.next.getNextEvent().inAct(currentTime);
            }
    }

    @Override
    public void doStatistics(double delta) {
        this.meanQueue = this.meanQueue + this.queue * delta;
    }
    @Override
    public void printResult() {
        super.printResult();
        System.out.println("failure = " + this.failure);
    }

}
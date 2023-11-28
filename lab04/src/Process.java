public class Process extends Event {
    public Process(double delay, String name) {
        super(delay, name);
        this.eventTime = Double.MAX_VALUE;
        this.distributionType = DistributionType.EXPONENTIAL;
    }

    @Override
    public void outAct(double tcurr) {
        super.outAct(tcurr);
            this.state = 1;
            double delay = getDelay();
            this.eventTime = tcurr + delay;
            totalWorkTime += delay;
            if (this.next != null) {
                this.next.getNextEvent().inAct(tcurr);
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
package bank;

public class Process extends Element {
    public Process(double delay, String name) {
        super(delay, name);
        this.tstate = Double.MAX_VALUE;
    }

    @Override
    public void outAct(double tcurr) {
        super.outAct(tcurr);
            this.state = 1;
            double delay = getDelay();
            this.tstate = tcurr + delay;
            totalWorkTime += delay;
            if (this.next != null) {
                this.next.getNextElement().inAct(tcurr);
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
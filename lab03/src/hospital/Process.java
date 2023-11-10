package hospital;

public class Process extends Event {

    public Process(double delay, String name) {
        super(delay, name);
        this.eventTime = Double.MAX_VALUE;
    }

    @Override
    public void outAct(double tcurr) {
        super.outAct(tcurr);
            this.state = 1;
            double delay = getDelay();
            this.eventTime = tcurr + delay;
            totalWorkTime += delay;
            if (this.next != null) {
                if (name.equals("Go to doctor"))
                {
                    double rand = Math.random();
                    if( rand < 0.5)
                        this.currentClientType = ClientType.FIRST;
                    else
                        this.currentClientType = ClientType.SECOND;
                }
                Event thisNext = this.next.getNextElement(currentClientType);
                if(thisNext != null) {
                    thisNext.inAct(tcurr, currentClientType);
                }
                else {
                    Model.timeOut.add(tcurr);
                }
            }
            else{
                Model.timeOut.add(tcurr);
            }
    }

    @Override
    public void doStatistics(double delta) {
        this.meanQueue = this.meanQueue + this.queue.size() * delta;
    }
    @Override
    public void printResult() {
        super.printResult();
        System.out.println("failure = " + this.failure);
    }

    public void setNextElement(NextEventsOnClientType next) {
        this.next = next;
    }
}
package hospital;

import org.w3c.dom.events.Event;

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
                if (name.equals("Go to doctor"))
                {
                    double rand = Math.random();
                    if( rand < 0.5)
                        this.currentClientType = ClientType.FIRST;
                    else
                        this.currentClientType = ClientType.SECOND;
                }
                Element thisNext = this.next.getNextElement(currentClientType);
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

    public void setNextElement(NextElementsOnClientType next) {
        this.next = next;
    }
}
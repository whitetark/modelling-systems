package hospital;
import java.util.List;

public class Create extends Element{
    public Create(double delay, String name) {
        super(delay, name);
    }
    @Override
    public void outAct(double tcurr, ClientType clientType1) {
        super.outAct(tcurr);
        Model.timeIn.add(tcurr);
        double delay = getDelay();
        totalWorkTime += delay;
        tstate = tcurr + delay;
        ClientType clientType = getClientByProbability();
        Element nextEl = next.getNextElement(clientType);
        nextEl.inAct(tcurr, clientType);
    }
    @Override
    public void doStatistics(double delta) {
        if (queue != null) {
            meanQueue = meanQueue + queue.size() * delta;
        }
    }

    public ClientType getClientByProbability() {
        double probability = Math.random();
        if (probability < 0.5) {
            return ClientType.FIRST;
        } else if (probability < 0.6) {
            return ClientType.SECOND;
        } else {
            return ClientType.THIRD;
        }
    }

}
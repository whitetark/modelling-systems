package hospital;

public class Create extends Event {
    public Create(double delay, String name) {
        super(delay, name);
    }
    @Override
    public void outAct(double currentTime, ClientType clientType1) {
        super.outAct(currentTime);
        Model.timeIn.add(currentTime);
        double delay = getDelay();
        totalWorkTime += delay;
        eventTime = currentTime + delay;
        ClientType clientType = getClientByProbability();
        Event nextEl = next.getNextElement(clientType);
        nextEl.inAct(currentTime, clientType);
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
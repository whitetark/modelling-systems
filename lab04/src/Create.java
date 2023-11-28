public class Create extends Event {
    public Create(double delay, String name) {
        super(delay, name);
    }
    @Override
    public void outAct(double currentTime) {
        super.outAct(currentTime);
        double delay = getDelay();
        totalWorkTime += delay;
        eventTime = currentTime + delay;
        next.getNextEvent().inAct(currentTime);
    }
    @Override
    public void doStatistics(double delta) {
        meanQueue = meanQueue + queue * delta;
    }
}
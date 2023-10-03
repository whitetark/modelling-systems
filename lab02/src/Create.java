import java.util.List;

public class Create extends Event{
    public Create(double delay, String name) {
        super(delay, name);
    }
    @Override
    public void outAct(double currentTime, List<Event> events) {
        super.outAct(currentTime, events);
        eventTime = currentTime + getDelay();
        next.inAct(currentTime);
    }
    @Override
    public void doStatistics(double delta) {
        meanQueue += queue * delta;
    }
}

package bank;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Create create = new Create(0.5, "Create", DistributionType.EXPONENTIAL);
        Process process1 = new Process(0.3, "Cashier 1", DistributionType.EXPONENTIAL);
        Process process2 = new Process(0.3, "Cashier 2", DistributionType.EXPONENTIAL);

        create.eventTime = 0.1;

        process1.state = 1;
        process2.state = 1;

        process1.eventTime = FunRand.norm(1, 0.3);
        process2.eventTime = FunRand.norm(1, 0.3);

        //create process first because bank.MultiTaskProcessor gets time from process
        MultiTaskProcessor multiTaskProcessor1 = new MultiTaskProcessor(List.of(process1), "Window1", 3);
        MultiTaskProcessor multiTaskProcessor2 = new MultiTaskProcessor(List.of(process2), "Window2", 3);

        multiTaskProcessor1.queue = 2;
        multiTaskProcessor2.queue = 2;

        NextEvent nextEvent1 = new NextEvent(multiTaskProcessor1,0.5,  2);
        NextEvent nextEvent2 = new NextEvent(multiTaskProcessor2, 0.5, 1);

        NextEvents nextEvents = new NextEvents(List.of(nextEvent1, nextEvent2), NextEventsType.PRIORITY_WITH_QUEUE);

        create.setNextEvent(nextEvents);

        List<MultiTaskProcessor> multiTaskProcessors = List.of(multiTaskProcessor1, multiTaskProcessor2);
        Model model = new Model(create, multiTaskProcessors);
        model.simulate(1000);
    }
}
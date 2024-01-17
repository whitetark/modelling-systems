import java.util.List;

public class Main {

    public static void main(String[] args) {
        Create create = new Create(2, "Create", DistributionType.EXPONENTIAL);
        Process process1 = new Process(9, "Cashier 1", DistributionType.EXPONENTIAL);
        Process process2 = new Process(9, "Cashier 2", DistributionType.EXPONENTIAL);
        Process process3 = new Process(9, "Cashier 3", DistributionType.EXPONENTIAL);
        Process process4 = new Process(9, "Cashier 4", DistributionType.EXPONENTIAL);
        Process process5 = new Process(9, "Cashier 5", DistributionType.EXPONENTIAL);

        create.eventTime = 2;

        process1.state = 1;
        process2.state = 1;
        process3.state = 1;
        process4.state = 1;
        process5.state = 1;

        process1.eventTime = FunRand.exp(9);
        process2.eventTime = FunRand.exp(9);
        process3.eventTime = FunRand.exp(9);
        process4.eventTime = FunRand.exp(9);
        process5.eventTime = FunRand.exp(9);

        MultiTaskProcessor multiTaskProcessor1 = new MultiTaskProcessor(List.of(process1), "Window1", 2);
        MultiTaskProcessor multiTaskProcessor2 = new MultiTaskProcessor(List.of(process2), "Window2", 2);
        MultiTaskProcessor multiTaskProcessor3 = new MultiTaskProcessor(List.of(process3), "Window3", 2);
        MultiTaskProcessor multiTaskProcessor4 = new MultiTaskProcessor(List.of(process4), "Window4", 2);
        MultiTaskProcessor multiTaskProcessor5 = new MultiTaskProcessor(List.of(process5), "Window5", 2);

        multiTaskProcessor1.queue = 1;
        multiTaskProcessor2.queue = 1;
        multiTaskProcessor3.queue = 1;
        multiTaskProcessor4.queue = 1;
        multiTaskProcessor5.queue = 1;

        NextEvent nextEvent1 = new NextEvent(multiTaskProcessor1,0.2,  1);
        NextEvent nextEvent2 = new NextEvent(multiTaskProcessor2, 0.2, 1);
        NextEvent nextEvent3 = new NextEvent(multiTaskProcessor3,0.2,  1);
        NextEvent nextEvent4 = new NextEvent(multiTaskProcessor4, 0.2, 1);
        NextEvent nextEvent5 = new NextEvent(multiTaskProcessor5,0.2,  1);

        NextEvents nextEvents = new NextEvents(List.of(nextEvent1, nextEvent2, nextEvent3, nextEvent4, nextEvent5), NextEventsType.PRIORITY_WITH_QUEUE);

        create.setNextEvent(nextEvents);

        List<MultiTaskProcessor> multiTaskProcessors = List.of(multiTaskProcessor1, multiTaskProcessor2, multiTaskProcessor3, multiTaskProcessor4, multiTaskProcessor5);
        Model model = new Model(create, multiTaskProcessors);
        model.simulate(1000);
    }
}
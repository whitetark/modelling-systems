package bank;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Create create = new Create(0.5, "Create");
        create.setDistributionType(DistributionType.EXPONENTIAL);
        Process process1_1 = new Process(0.3, "Cashier1");
        process1_1.setDistributionType(DistributionType.EXPONENTIAL);
        Process process1_2 = new Process(0.3, "Cashier2");
        process1_2.setDistributionType(DistributionType.EXPONENTIAL);

        create.tstate = 0.1;

        process1_1.state = 1;
        process1_2.state = 1;

        process1_1.tstate = FunRand.norm(1, 0.3);
        process1_2.tstate = FunRand.norm(1, 0.3);

        //create process first because bank.MultiTaskProcessor gets time from process
        MultiTaskProcessor multiTaskProcessor1 = new MultiTaskProcessor(List.of(process1_1), "MultiProcess1", 3);
        MultiTaskProcessor multiTaskProcessor2 = new MultiTaskProcessor(List.of(process1_2), "MultiProcess2", 3);

        multiTaskProcessor1.queue = 2;
        multiTaskProcessor2.queue = 2;

        NextElement nextElement1 = new NextElement(multiTaskProcessor1,0.5,  2);
        NextElement nextElement2 = new NextElement(multiTaskProcessor2, 0.5, 1);

        NextElements nextElements = new NextElements(List.of(nextElement1, nextElement2), NextElementsType.PRIORITY_WITH_QUEUE);

        create.setNextElement(nextElements);

        List<MultiTaskProcessor> multiTaskProcessors = List.of(multiTaskProcessor1, multiTaskProcessor2);
        Model model = new Model(create, multiTaskProcessors);
        model.simulate(1000);
    }
}
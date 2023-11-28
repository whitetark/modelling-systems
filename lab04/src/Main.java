import java.util.*;

public class Main {

    public static void main(String[] args) {
        Map<Integer, Double> results = new HashMap<>();
        int k = 5;

        for (int N = 10; N <= 300; N += 10) {
            double[] Nresults = new double[k];

            for (int i = 0; i < k; i++) {
                Model model = createOneChainModel(N);

                long startTime = System.currentTimeMillis();

                model.simulate(50000);

                long endTime = System.currentTimeMillis();
                Nresults[i] = endTime - startTime;
            }

            double averageTime = calculateAverage(Nresults);
            results.put(N, averageTime);
        }

        printResults(results);
    }

    public static Model createTwoChainModel(int N) {
        Create create = new Create(1, "Create");
        create.setDistributionType(DistributionType.EXPONENTIAL);
        List<NextElement> nextElementList = new ArrayList<>();
        List<MultiTaskProcessor> multiTaskProcessors1 = new ArrayList<>();
        List<MultiTaskProcessor> multiTaskProcessors2 = new ArrayList<>();
        Process Process1 = new Process(1, "Process1");
        Process1.setDistributionType(DistributionType.EXPONENTIAL);

        for (int i = 0; i < N; i++) {
            Process process = new Process(1, "Process" + i);
            process.setDistributionType(DistributionType.EXPONENTIAL);

            MultiTaskProcessor multiTaskProcessor = new MultiTaskProcessor(List.of(process), "MultiTaskProcessor" + i, Integer.MAX_VALUE);

            NextElement hostesNext = new NextElement(multiTaskProcessor, 1);
            nextElementList.add(hostesNext);

            multiTaskProcessors1.add(multiTaskProcessor);
        }
        for (int i = 0; i < N; i++) {
            Process process = new Process(1, "Process" + i);
            process.setDistributionType(DistributionType.EXPONENTIAL);

            MultiTaskProcessor multiTaskProcessor = new MultiTaskProcessor(List.of(process), "MultiTaskProcessor" + i, Integer.MAX_VALUE);

            NextElement hostesNext = new NextElement(multiTaskProcessor, 1);
            nextElementList.add(hostesNext);

            multiTaskProcessors2.add(multiTaskProcessor);
        }
        create.setNextElement(new NextElements(nextElementList, NextElementsType.PRIORITY));

        for (int i = 0; i < N; i++) {
            Process1.setNextElement(new NextElements(List.of(new NextElement(multiTaskProcessors1.get(i), 1)), NextElementsType.PRIORITY));
            multiTaskProcessors1.get(i).setNextElement(new NextElements(List.of(new NextElement(multiTaskProcessors2.get(i), 1)), NextElementsType.PRIORITY));
        }

        multiTaskProcessors1.addAll(multiTaskProcessors2);
        return new Model(create, multiTaskProcessors1);
    }

    public static Model createOneChainModel(int N) {
        Create create = new Create(1, "Create");
        create.setDistributionType(DistributionType.EXPONENTIAL);
        List<NextElement> nextElementList = new ArrayList<>();
        List<MultiTaskProcessor> multiTaskProcessors = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Process process = new Process(1, "Process" + i);
            process.setDistributionType(DistributionType.EXPONENTIAL);

            MultiTaskProcessor multiTaskProcessor = new MultiTaskProcessor(List.of(process), "MultiTaskProcessor" + i, Integer.MAX_VALUE);

            NextElement hostesNext = new NextElement(multiTaskProcessor, 1);
            nextElementList.add(hostesNext);

            multiTaskProcessors.add(multiTaskProcessor);
        }
        create.setNextElement(new NextElements(nextElementList, NextElementsType.PRIORITY));
        return new Model(create, multiTaskProcessors);
    }

    private static double calculateAverage(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    private static void printResults(Map<Integer, Double> results) {
        Map<Integer, Double> sortedResults = new TreeMap<>(results);
        System.out.println("Results:");
        System.out.println("N\tAverage Time (ms)");

        System.out.println("N" + ";" + "time" + ";");
        for (Map.Entry<Integer, Double> entry : sortedResults.entrySet()) {
            System.out.println(entry.getKey() + ";" + entry.getValue() + ";");
        }

        for (Map.Entry<Integer, Double> entry : sortedResults.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

}
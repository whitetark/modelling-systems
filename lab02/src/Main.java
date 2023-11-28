import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //task1();
        //task3();
        task4();
        //task5();
        //task6();
    }

    public static void task1 (){
        Create create = new Create(5, "Create");
        Process process1 = new Process(5, "Process1", 5.0, 1);

        create.setNextElement(process1);

        List<Event> events = List.of(process1);
        Model model = new Model(create, events);
        model.simulate(1000);
    }

    public static void task3 (){
        Create create = new Create(5, "Create");
        Process process1 = new Process(5, "Process1", 5.0, 1);
        Process process2 = new Process(5, "Process2", 5.0, 1);
        Process process3 = new Process(5, "Process3", 5.0, 1);

        create.setNextElement(process1);
        process1.setNextElement(process2);
        process2.setNextElement(process3);

        List<Event> events = List.of(process1, process2, process3);
        Model model = new Model(create, events);
        model.simulate(1000);
    }

    public static void task4(){
        JFrame frame = new JFrame();
        int n = 15;
        Double[] delay_create = {2.0, 10.0, 1.0, 6.0, 7.0, 8.0, 1.0, 3.0, 0.5, 5.0, 5.0, 4.0, 4.0, 4.0, 4.0};
        Double[] delay_process1 = {2.0, 1.0, 5.0, 4.0, 4.0, 5.0, 0.7, 0.4, 4.0, 1.0, 4.0, 4.0, 4.0, 0.3, 4.0};
        Double[] delay_process2 = {2.0, 1.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 6.0, 4.0, 0.5, 4.0, 4.0, 4.0, 4.0};
        Double[] delay_process3 = {2.0, 1.0, 4.0, 4.0, 10.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.7, 4.0, 4.0, 4.0};
        Double[] maxQ_list1 = {5.0, 4.0, 5.0, 5.0, 5.0, 15.0, 5.0, 9.0, 6.0, 6.0, 5.0, 7.0, 1.0, 5.0, 5.0};
        Double[] maxQ_list2 = {5.0, 3.0, 7.0, 4.0, 5.0, 8.0, 1.0, 5.0, 5.0, 6.0, 7.0, 5.0, 8.0, 1.0, 5.0};
        Double[] maxQ_list3 = {5.0, 5.0, 5.0, 8.0, 5.0, 6.0, 5.0, 10.0, 5.0, 6.0, 5.0, 5.0, 5.0, 5.0, 1.0};

        String[] columnNames = {"C_D", "P1_D", "P2_D", "P3_D", "P1_Q", "P2_Q", "P3_Q",
                "P1_Ok", "P1_Err", "P1_Avg_Q", "P1_FProb", "P1_Avg_L",
                "P2_Ok", "P2_Err", "P2_Avg_Q", "P2_FProb", "P2_Avg_L",
                "P3_Ok", "P3_Err",  "P3_Avg_Q", "P3_FProb", "P3_Avg_L"};

        Object[][] data = new Object[n][columnNames.length];

        for (int i = 0; i < n; i++) {
            Create create = new Create(delay_create[i], "Create");
            Process process1 = new Process(delay_process1[i], "Process1", maxQ_list1[i], 2);
            Process process2 = new Process(delay_process2[i], "Process2", maxQ_list2[i], 2);
            Process process3 = new Process(delay_process3[i], "Process3", maxQ_list3[i], 2);

            create.setNextElement(process1);
            process1.setNextElement(process2);
            process2.setNextElement(process3);

            List<Event> events = List.of(process1, process2, process3);
            Model model = new Model(create, events);
            model.simulate(1000);

            data[i] = new Object[]{delay_create[i], delay_process1[i], delay_process2[i], delay_process3[i],
                    maxQ_list1[i], maxQ_list2[i], maxQ_list3[i],
                    process1.served, process1.failure, process1.meanQueue/1000, process1.failure / ((double) process1.served + process1.failure),
                    (process1.meanLoad/process1.workerStates.size())/1000,
                    process2.served, process2.failure, process2.meanQueue/1000, process2.failure / ((double) process2.served + process2.failure),
                    (process1.meanLoad/process1.workerStates.size())/1000,
                    process3.served, process3.failure, process3.meanQueue/1000, process3.failure / ((double) process3.served + process3.failure),
                    (process1.meanLoad/process1.workerStates.size())/1000};
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void task5 (){
        Create create = new Create(5, "Create");
        Process process1 = new Process(5, "Process1", 5.0, 2);

        create.setNextElement(process1);

        List<Event> events = List.of(process1);
        Model model = new Model(create, events);
        model.simulate(1000);
    }

    public static void task6(){
        Create create = new Create(5, "Create");
        Process process1 = new Process(5, "Process1", 5.0, 3);
        Process process2 = new Process(5, "Process2", 5.0, 3);
        Process process3 = new Process(5, "Process3", 5.0, 3);

        List<Event> nextEvents = List.of(process2, process3);

        create.setNextElement(process1);
        process1.setNextEvent(nextEvents);

        List<Event> events = List.of(process1, process2, process3);
        Model model = new Model(create, events);
        model.simulate(1000);
    }
}
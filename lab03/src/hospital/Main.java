package hospital;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //they are comming to hospital
        Create patient = new Create(15, "Patient");

        //delay zero because sets later
        Process firstDoctor = new Process(0, "First Doctor");
        Process secondDoctor = new Process(0, "Second Doctor");

        // now they are comming to doctor
        MultiTaskProcessor multiDoctorProcessor = new MultiTaskProcessor(List.of(firstDoctor, secondDoctor), "multiDoctorProcessor", Integer.MAX_VALUE);
        multiDoctorProcessor.isDoctor = true;

        NextElements firstDoctorNextElements = new NextElements(List.of(new NextElement(multiDoctorProcessor, 1)), NextElementsType.PRIORITY);

        Map map = new HashMap<ClientType, NextElements>();
        map.put(ClientType.FIRST, firstDoctorNextElements);
        map.put(ClientType.SECOND, firstDoctorNextElements);
        map.put(ClientType.THIRD, firstDoctorNextElements);

        NextElementsOnClientType afterComming = new NextElementsOnClientType(map);

        patient.setNextElement(afterComming);

        Process firstAccompanying = new Process(0, "First Accompanying");
        Process secondAccompanying = new Process(0, "Second Accompanying");
        Process thirdAccompanying = new Process(0, "Third Accompanying");
        firstAccompanying.setUniformDistribution(3, 8);
        secondAccompanying.setUniformDistribution(3, 8);
        thirdAccompanying.setUniformDistribution(3, 8);

        MultiTaskProcessor multiAccompanyingProcessor = new MultiTaskProcessor(List.of(firstAccompanying, secondAccompanying, thirdAccompanying), "multiAccompanyingProcessor", Integer.MAX_VALUE);

        NextElements firstAccompanyingNextElements = new NextElements(List.of(new NextElement(multiAccompanyingProcessor, 1)), NextElementsType.PRIORITY);

        Process goToLab = new Process(0, "Go to laboratory");
        goToLab.setUniformDistribution(2, 5);
        MultiTaskProcessor multiGoToLabProcessor = new MultiTaskProcessor(List.of(goToLab), "multiGoToLabProcessor", Integer.MAX_VALUE);

        NextElements firstGoToLabNextElements = new NextElements(List.of(new NextElement(multiGoToLabProcessor, 1)), NextElementsType.PRIORITY);

        Map map2 = new HashMap<ClientType, NextElements>();
        map2.put(ClientType.FIRST, firstAccompanyingNextElements);
        map2.put(ClientType.SECOND, firstGoToLabNextElements);
        map2.put(ClientType.THIRD, firstGoToLabNextElements);

        NextElementsOnClientType afterDoctor = new NextElementsOnClientType(map2);

        firstDoctor.setNextElement(afterDoctor);
        secondDoctor.setNextElement(afterDoctor);

        Process registrationToLab = new Process(0, "registrationToLaboratory");
        registrationToLab.setErlangDistribution(4.5, 3);
        MultiTaskProcessor multiregistrationToLabProcessor = new MultiTaskProcessor(List.of(registrationToLab), "multiregistrationToLabProcessor", Integer.MAX_VALUE);

        NextElements firstRegistrationToLabNextElements = new NextElements(List.of(new NextElement(multiregistrationToLabProcessor, 1)), NextElementsType.PRIORITY);

        Map map2_2 = new HashMap<ClientType, NextElements>();
        map2_2.put(ClientType.FIRST, null);
        map2_2.put(ClientType.SECOND, firstRegistrationToLabNextElements);
        map2_2.put(ClientType.THIRD, firstRegistrationToLabNextElements);


        NextElementsOnClientType afterComeToLab = new NextElementsOnClientType(map2_2);

        goToLab.setNextElement(afterComeToLab);

        Process firstLaborant = new Process(0, "First Laborant");
        Process secondLaborant = new Process(0, "Second Laborant");
        firstLaborant.setErlangDistribution(4, 2);
        secondLaborant.setErlangDistribution(4, 2);

        MultiTaskProcessor multiLaborantProcessor = new MultiTaskProcessor(List.of(firstLaborant, secondLaborant), "multiLaborantProcessor", Integer.MAX_VALUE);

        NextElements firstLaborantNextElements = new NextElements(List.of(new NextElement(multiLaborantProcessor, 1)), NextElementsType.PRIORITY);

        Map map3 = new HashMap<ClientType, NextElements>();
        map3.put(ClientType.FIRST, null);
        map3.put(ClientType.SECOND, firstLaborantNextElements);
        map3.put(ClientType.THIRD, firstLaborantNextElements);

        NextElementsOnClientType afterLab = new NextElementsOnClientType(map3);

        registrationToLab.setNextElement(afterLab);

        Process goToDoctor = new Process(0, "Go to doctor");
        goToDoctor.setUniformDistribution(2, 5);

        MultiTaskProcessor multiGoToDoctorProcessor = new MultiTaskProcessor(List.of(goToDoctor), "multiGoToDoctorProcessor", Integer.MAX_VALUE);

        NextElements firstGoToDoctorNextElements = new NextElements(List.of(new NextElement(multiGoToDoctorProcessor, 1)), NextElementsType.PRIORITY);

        Map map3_2 = new HashMap<ClientType, NextElements>();
        map3_2.put(ClientType.FIRST, null);
        map3_2.put(ClientType.SECOND, firstGoToDoctorNextElements);
        map3_2.put(ClientType.THIRD, firstGoToDoctorNextElements);

        NextElementsOnClientType afterLab2 = new NextElementsOnClientType(map3_2);
        firstLaborant.setNextElement(afterLab2);
        secondLaborant.setNextElement(afterLab2);

        Map map4 = new HashMap<ClientType, NextElements>();
        map4.put(ClientType.FIRST, firstDoctorNextElements);
        map4.put(ClientType.SECOND, null);
        map4.put(ClientType.THIRD, null);

        NextElementsOnClientType afterCome = new NextElementsOnClientType(map4);
        goToDoctor.setNextElement(afterCome);


        Model model = new Model(patient, List.of(multiDoctorProcessor, multiAccompanyingProcessor, multiGoToDoctorProcessor, multiLaborantProcessor, multiGoToLabProcessor, multiregistrationToLabProcessor));
        model.simulate(10000);
    }
}
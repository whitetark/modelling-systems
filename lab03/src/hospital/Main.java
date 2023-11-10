package hospital;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //They are coming to hospital
        Create patient = new Create(15, "Patient");

        //Delay zero because sets later
        Process firstDoctor = new Process(0, "First Doctor");
        Process secondDoctor = new Process(0, "Second Doctor");

        //Now they are coming to doctor
        MultiTaskProcessor dutyDoctorsArc = new MultiTaskProcessor(List.of(firstDoctor, secondDoctor), "Duty Doctors Processor", Integer.MAX_VALUE);
        dutyDoctorsArc.isDoctor = true;

        NextEvents doctorNextEvents = new NextEvents(List.of(new NextEvent(dutyDoctorsArc, 1)), NextEventsType.PRIORITY);

        Map clientsCameToHospital = new HashMap<ClientType, NextEvents>();
        clientsCameToHospital.put(ClientType.FIRST, doctorNextEvents);
        clientsCameToHospital.put(ClientType.SECOND, doctorNextEvents);
        clientsCameToHospital.put(ClientType.THIRD, doctorNextEvents);

        NextEventsOnClientType afterComing = new NextEventsOnClientType(clientsCameToHospital);

        patient.setNextElement(afterComing);

        //Attendants are taken to the rooms

        Process firstAccompanying = new Process(0, "First Accompanying");
        Process secondAccompanying = new Process(0, "Second Accompanying");
        Process thirdAccompanying = new Process(0, "Third Accompanying");
        firstAccompanying.setUniformDistribution(3, 8);
        secondAccompanying.setUniformDistribution(3, 8);
        thirdAccompanying.setUniformDistribution(3, 8);

        MultiTaskProcessor attendantsArc = new MultiTaskProcessor(List.of(firstAccompanying, secondAccompanying, thirdAccompanying), "Accompanying Processor", Integer.MAX_VALUE);

        NextEvents attendantsNextEvents = new NextEvents(List.of(new NextEvent(attendantsArc, 1)), NextEventsType.PRIORITY);

        Process goToLab = new Process(0, "Go to laboratory");
        goToLab.setUniformDistribution(2, 5);
        MultiTaskProcessor goToLabArc = new MultiTaskProcessor(List.of(goToLab), "Go To Lab Processor", Integer.MAX_VALUE);

        NextEvents goToLabNextEvents = new NextEvents(List.of(new NextEvent(goToLabArc, 1)), NextEventsType.PRIORITY);

        Map clientsAfterDoctor = new HashMap<ClientType, NextEvents>();
        clientsAfterDoctor.put(ClientType.FIRST, attendantsNextEvents);
        clientsAfterDoctor.put(ClientType.SECOND, goToLabNextEvents);
        clientsAfterDoctor.put(ClientType.THIRD, goToLabNextEvents);

        NextEventsOnClientType afterDoctor = new NextEventsOnClientType(clientsAfterDoctor);

        firstDoctor.setNextElement(afterDoctor);
        secondDoctor.setNextElement(afterDoctor);

        //2nd and 3rd types are coming to registration

        Process registrationToLab = new Process(0, "Registration to Laboratory");
        registrationToLab.setErlangDistribution(4.5, 3);
        MultiTaskProcessor registrationArc = new MultiTaskProcessor(List.of(registrationToLab), "Registration To Lab Processor", Integer.MAX_VALUE);

        NextEvents registrationNextEvents = new NextEvents(List.of(new NextEvent(registrationArc, 1)), NextEventsType.PRIORITY);

        Map clientsCameToRegistration = new HashMap<ClientType, NextEvents>();
        clientsCameToRegistration.put(ClientType.FIRST, null);
        clientsCameToRegistration.put(ClientType.SECOND, registrationNextEvents);
        clientsCameToRegistration.put(ClientType.THIRD, registrationNextEvents);

        NextEventsOnClientType afterComeToLab = new NextEventsOnClientType(clientsCameToRegistration);

        goToLab.setNextElement(afterComeToLab);

        //2nd and 3rd types are coming to lab

        Process firstLaborant = new Process(0, "First Laborant");
        Process secondLaborant = new Process(0, "Second Laborant");
        firstLaborant.setErlangDistribution(4, 2);
        secondLaborant.setErlangDistribution(4, 2);

        MultiTaskProcessor laboratoryArc = new MultiTaskProcessor(List.of(firstLaborant, secondLaborant), "Laboratory Processor", Integer.MAX_VALUE);

        NextEvents laboratoryNextEvents = new NextEvents(List.of(new NextEvent(laboratoryArc, 1)), NextEventsType.PRIORITY);

        Map clientsCameToLab = new HashMap<ClientType, NextEvents>();
        clientsCameToLab.put(ClientType.FIRST, null);
        clientsCameToLab.put(ClientType.SECOND, laboratoryNextEvents);
        clientsCameToLab.put(ClientType.THIRD, laboratoryNextEvents);

        NextEventsOnClientType afterLab = new NextEventsOnClientType(clientsCameToLab);

        registrationToLab.setNextElement(afterLab);

        //2nd and 3rd types are going to doctor

        Process goToDoctor = new Process(0, "Go to doctor");
        goToDoctor.setUniformDistribution(2, 5);

        MultiTaskProcessor goToDoctorArc = new MultiTaskProcessor(List.of(goToDoctor), "Go To Doctor Processor", Integer.MAX_VALUE);

        NextEvents goToDoctorNextEvents = new NextEvents(List.of(new NextEvent(goToDoctorArc, 1)), NextEventsType.PRIORITY);

        Map clientsGoToDoctor = new HashMap<ClientType, NextEvents>();
        clientsGoToDoctor.put(ClientType.FIRST, null);
        clientsGoToDoctor.put(ClientType.SECOND, goToDoctorNextEvents);
        clientsGoToDoctor.put(ClientType.THIRD, goToDoctorNextEvents);

        NextEventsOnClientType afterLab2 = new NextEventsOnClientType(clientsGoToDoctor);
        firstLaborant.setNextElement(afterLab2);
        secondLaborant.setNextElement(afterLab2);

        //Doctor accepts other clients

        Map map4 = new HashMap<ClientType, NextEvents>();
        map4.put(ClientType.FIRST, doctorNextEvents);
        map4.put(ClientType.SECOND, null);
        map4.put(ClientType.THIRD, null);

        NextEventsOnClientType afterCome = new NextEventsOnClientType(map4);
        goToDoctor.setNextElement(afterCome);

        Model model = new Model(patient, List.of(dutyDoctorsArc, attendantsArc, goToDoctorArc, laboratoryArc, goToLabArc, registrationArc));
        model.simulate(10000);
    }
}
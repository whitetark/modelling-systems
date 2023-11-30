/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7;

/**
 *
 * @author leokr
 */
import LibNet.NetLibrary;
import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.ExceptionInvalidTimeDelay;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import java.util.ArrayList;

public class buses {

    
    public static void main(String[] args) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling  
        PetriObjModel model = null;
        int choise = 1;  // вот это менять чтоб прыгать между дефолт моделью и изменённой
        int people = 10;
        model = getModel(people);
        
        model.setIsProtokol(false);
        double timeModeling = 600;
        model.go(timeModeling);

        //Цей фрагмент для виведення результатів моделювання на консоль
        System.out.println("Transfered to Kyiv");
        System.out.println(model.getListObj().get(0).getNet().getListP()[2].getMark());
        System.out.println("Transfered to Lviv");
        System.out.println(model.getListObj().get(1).getNet().getListP()[2].getMark());
        System.out.println("Lost Money");
        System.out.println(model.getListObj().get(0).getNet().getListP()[4].getMark());
        System.out.println("Gain Money");
        System.out.println(model.getListObj().get(2).getNet().getListP()[5].getMark());
        System.out.println("Average Queue in Kyiv");
        System.out.println(model.getListObj().get(0).getNet().getListP()[3].getMean());
        System.out.println("Average Queue in Lviv");
        System.out.println(model.getListObj().get(1).getNet().getListP()[3].getMean());
        System.out.println("Waiting Time in Kyiv");
        System.out.println(model.getListObj().get(0).getNet().getListP()[3].getMean() * timeModeling
                / model.getListObj().get(0).getNet().getListP()[2].getMark());
        System.out.println("Waiting Time in Lviv");
        System.out.println(model.getListObj().get(1).getNet().getListP()[3].getMean() * timeModeling
                / model.getListObj().get(1).getNet().getListP()[2].getMark());
        System.out.println("Num Of Seats, When Waiting Time Will Be Min");
        System.out.println(getMinTime(choise));
    }
    
    public static int getMinTime(int choise) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        int peopleMin = Integer.MAX_VALUE;
        double timeMin = Double.MAX_VALUE;
        PetriObjModel model = null;

        for(int people = 1; people <= 25; people++){
           model = getModel(people);
           model.setIsProtokol(false);
           double timeModeling = 600;
           model.go(timeModeling);
           double time = model.getListObj().get(0).getNet().getListP()[3].getMean() * timeModeling
                / model.getListObj().get(0).getNet().getListP()[2].getMark() 
                   + model.getListObj().get(1).getNet().getListP()[3].getMean() * timeModeling
                / model.getListObj().get(1).getNet().getListP()[2].getMark();
           
           if(time < timeMin){
               timeMin = time;
               peopleMin = people;
           }
        }
        return peopleMin;
    }

    // метод для конструювання моделі масового обслуговування з 4 СМО 
    public static PetriObjModel getModel(int people) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<PetriSim>();

        list.add(new PetriSim(CreateBusStop("Kyiv", 0.5, 0.2)));
        list.add(new PetriSim(CreateBusStop("Lviv", 0.5, 0.2)));
        list.add(new PetriSim(CreateTravel("bus A in Kyiv", 1, people)));
        list.add(new PetriSim(CreateTravel("bus B in Kyiv", 1, people)));
        list.add(new PetriSim(CreateTravel("bus A in Lviv", 1, people)));
        list.add(new PetriSim(CreateTravel("bus B in Lviv", 1, people)));

        list.get(2).getNet().getListT()[0].setPriority(1);
        list.get(4).getNet().getListT()[0].setPriority(1);

        list.get(0).getNet().getListP()[3] = list.get(2).getNet().getListP()[0];
        list.get(0).getNet().getListP()[3] = list.get(3).getNet().getListP()[0];
        list.get(0).getNet().getListP()[4] = list.get(1).getNet().getListP()[4];

        list.get(1).getNet().getListP()[3] = list.get(4).getNet().getListP()[0];
        list.get(1).getNet().getListP()[3] = list.get(5).getNet().getListP()[0];

        list.get(2).getNet().getListP()[4] = list.get(4).getNet().getListP()[3];
        list.get(2).getNet().getListP()[3] = list.get(4).getNet().getListP()[4];

        list.get(3).getNet().getListP()[4] = list.get(5).getNet().getListP()[3];
        list.get(3).getNet().getListP()[3] = list.get(5).getNet().getListP()[4];

        list.get(3).getNet().getListP()[5] = list.get(2).getNet().getListP()[5];
        list.get(4).getNet().getListP()[5] = list.get(2).getNet().getListP()[5];
        list.get(5).getNet().getListP()[5] = list.get(2).getNet().getListP()[5];

        PetriObjModel model = new PetriObjModel(list);
        return model;
    }

    public static PetriNet CreateBusStop(String name, double mean, double deviation) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P", 1));
        d_P.add(new PetriP("потенційні пасажири", 0));
        d_P.add(new PetriP("загальна кількість", 0));
        d_P.add(new PetriP("черга пасажирів", 0));
        d_P.add(new PetriP("втрачений прибуток", 0));
        d_T.add(new PetriT("надходження пасажирів", mean));
        d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(deviation);
        d_T.add(new PetriT("надходження у чергу", 0.0));
        d_T.add(new PetriT("покидання зупинки", 0.0));
        d_T.get(2).setPriority(1);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 30));
        d_In.get(3).setInf(true);
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 20));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet(name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateTravel(String name, int buses, int people) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("черга пасажирів", 0));
        d_P.add(new PetriP("готовність відправитись", 0));
        d_P.add(new PetriP("вільний автобус", buses));
        d_P.add(new PetriP("автобус приїхав", 0));
        d_P.add(new PetriP("автобус в іншому місті", 0));
        d_P.add(new PetriP("отриманий прибуток", 0));
        d_T.add(new PetriT("посадка", 0.0));
        d_T.add(new PetriT("пареїзд", 20.0));
        d_T.get(1).setDistribution("unif", d_T.get(1).getTimeServ());
        d_T.get(1).setParamDeviation(5.0);
        d_T.add(new PetriT("висадка", 5.0));
        d_T.get(2).setDistribution("unif", d_T.get(2).getTimeServ());
        d_T.get(2).setParamDeviation(1.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), people));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(5), 500));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet(name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

}

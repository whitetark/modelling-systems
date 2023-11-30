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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

public class robots {

    public static void main(String[] args) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        firstMain();
    }

    public static void firstMain() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {

        // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling  
        PetriObjModel model = getModel();
        model.setIsProtokol(false);
        double timeModeling = 1000000;
        model.go(timeModeling);

        //Цей фрагмент для виведення результатів моделювання на консоль
        System.out.println("Total Arrived");
        System.out.println(model.getListObj().get(0).getNet().getListP()[2].getMark());
        System.out.println("Processed By Machine 1");
        System.out.println(model.getListObj().get(4).getNet().getListP()[3].getMark());
        System.out.println("Processed By Machine 2");
        System.out.println(model.getListObj().get(5).getNet().getListP()[3].getMark());
        System.out.println("Arrived to Storage");
        System.out.println(model.getListObj().get(3).getNet().getListP()[3].getMark());
        System.out.println("Percentage of Fully Processed");
        System.out.println((double) model.getListObj().get(3).getNet().getListP()[3].getMark()
                / model.getListObj().get(0).getNet().getListP()[2].getMark() * 100);
    }


    // метод для конструювання моделі
    public static PetriObjModel getModel() throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(CreateGenerator()));
        list.add(new PetriSim(Create1Transportation(6, "Transport start-1")));
        list.add(new PetriSim(Create1Transportation(7, "Transport 1-2")));
        list.add(new PetriSim(Create1Transportation(5, "Transport 2-finish")));
        list.add(new PetriSim(CreateProcess("norm", 60, 10, "process 1")));
        list.add(new PetriSim(CreateProcess("exp", 100, "process 2")));

        list.get(0).getNet().getListP()[1] = list.get(1).getNet().getListP()[0];
        list.get(1).getNet().getListP()[3] = list.get(4).getNet().getListP()[0];
        list.get(4).getNet().getListP()[1] = list.get(2).getNet().getListP()[0];
        list.get(2).getNet().getListP()[3] = list.get(5).getNet().getListP()[0];
        list.get(5).getNet().getListP()[1] = list.get(3).getNet().getListP()[0];

        PetriObjModel model = new PetriObjModel(list);
        return model;
    }

   
    public static PetriNet CreateGenerator() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P", 1));
        d_P.add(new PetriP("пункт прийому", 0));
        d_P.add(new PetriP("всього надійшло", 0));
        d_T.add(new PetriT("надходження", 40.0));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("Generator", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet Create1Transportation(double time, String name) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("деталі, що надійшли", 0));
        d_P.add(new PetriP("захоплені деталі", 0));
        d_P.add(new PetriP("доставлені деталі", 0));
        d_P.add(new PetriP("звільнені деталі", 0));
        d_P.add(new PetriP("вільні роботи", 1));
        d_P.add(new PetriP("звільнені роботи", 0));
        d_T.add(new PetriT("захоплення деталі", 8.0));
        d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(1.0);
        d_T.add(new PetriT("переміщення", time));
        d_T.add(new PetriT("вивільнення деталі", 8.0));
        d_T.get(2).setDistribution("unif", d_T.get(2).getTimeServ());
        d_T.get(2).setParamDeviation(1.0);
        d_T.add(new PetriT("переміщення", time));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        PetriNet d_Net = new PetriNet(name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateProcess(String dist, double mean, String name) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("черга на верстат", 0));
        d_P.add(new PetriP("оброблені верстатом", 0));
        d_P.add(new PetriP("вільні місця", 3));
        d_P.add(new PetriP("всього оброблено", 0));
        d_T.add(new PetriT("обробка", mean));
        d_T.get(0).setDistribution(dist, d_T.get(0).getTimeServ());
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(3), 1));
        PetriNet d_Net = new PetriNet(name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateProcess(String dist, double mean, double deviation, String name) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        PetriNet d_Net = CreateProcess(dist, mean, name);
        d_Net.getListT()[0].setParamDeviation(deviation);
        return d_Net;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static PetriNet Create2Transportation() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("деталі, що надійшли", 0));
        d_P.add(new PetriP("захоплені деталі", 0));
        d_P.add(new PetriP("доставлені деталі", 0));
        d_P.add(new PetriP("звільнені деталі", 0));
        d_P.add(new PetriP("вільні роботи", 3));
        d_T.add(new PetriT("захоплення деталі", 8.0));
        d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(1.0);
        d_T.add(new PetriT("переміщення", 6.0));
        d_T.add(new PetriT("вивільнення деталі", 8.0));
        d_T.get(2).setDistribution("unif", d_T.get(2).getTimeServ());
        d_T.get(2).setParamDeviation(1.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        PetriNet d_Net = new PetriNet("Transportation", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

}

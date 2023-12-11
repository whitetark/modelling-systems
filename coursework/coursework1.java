/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LibNet;
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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
/**
 *
 * @author white
 */
public class coursework1 {

    public static void main(String[] args) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        int timeModeling = 1440;
        int numOfExp = 5;
        int numOfSituations = 16;
        
        //showStatistics(numOfExp, numOfSituations, timeModeling);
        //validateModel(timeModeling); //Time Verification
        //findChebishev(timeModeling); //Chebishev
    }
    
    public static void showStatistics(int numOfExp, int numOfSituations, int timeModeling) throws
        ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        JFrame frame = new JFrame();
        String[] columnNames = {"CreateDelay", "FirstRegDelay", "SecondRegDelay", "FullRegDelay","FirstRegQ", "SecondRegQ", "FullRegQ", "MaxQ1" ,"MeanQ1" ,"LeftQ1" ,"MaxQ2" ,"MeanQ2" ,"LeftQ2", "NumOfSecond" ,"NumOfFull", "FProb"};
        double[] createDelay = {10.0, 30.0, 40.0, 40.0, 20.0, 50.0, 30.0, 30.0, 10.0, 40.0, 30.0, 20.0, 50.0, 30.0, 20.0, 30.0};
        double[] firstRegDelay = {20.0, 30.0, 40.0, 10.0, 20.0, 30.0, 30.0, 50.0, 40.0, 30.0, 20.0, 30.0, 10.0, 20.0, 30.0, 30.0};
        int[] firstRegQ = {1, 2, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 3, 2, 1, 1};
        double[] secondRegDelay = {20.0, 30.0, 40.0, 30.0, 50.0, 30.0, 20.0, 10.0, 40.0, 30.0, 40.0, 30.0, 10.0, 20.0, 10.0, 30.0};
        int[] secondRegQ = {1, 2, 1, 1, 3, 2, 1, 2, 1, 2, 3, 2, 2, 1, 3, 1};
        double[] fullRegDelay = {80.0, 100.0, 140.0, 60.0, 100.0, 100.0, 80.0, 140.0, 100.0, 80.0, 80.0, 100.0, 100.0, 80.0, 120.0, 100.0};
        int[] fullRegQ = {2, 1, 1, 3, 2, 1, 2, 3, 1, 1, 2, 2, 1, 1, 1, 3};
        Object[][] data = new Object[numOfSituations*numOfExp][columnNames.length];
        int index = 0;
        
        ArrayList<Double> finProbs = new ArrayList<>();
        ArrayList<Double> finProbsAvgGrouped = new ArrayList<>();
        ArrayList<Double> groupDeviation = new ArrayList<>();
        ArrayList<Double> finProbsExp;
        
        int totalSumOfProcessed = 0;
        int totalSumOfStacked = 0;
        
        for(int i = 0; i < numOfSituations; i++){
            finProbsExp = new ArrayList<>();
            for(int j = 0; j < numOfExp; j++){
                PetriObjModel model = getModel(createDelay[i], firstRegDelay[i], firstRegQ[i], secondRegDelay[i], secondRegQ[i], fullRegDelay[i], fullRegQ[i]);
                    model.setIsProtokol(false);
                    model.go(timeModeling);
                    double failureProbability = model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark()
                    / (double)(model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark()
                    + model.getListObj().get(0).getNet().getListP()[3].getMark()) + model.getListObj().get(0).getNet().getListP()[2].getMark();
                    
                    totalSumOfProcessed = totalSumOfProcessed + model.getListObj().get(0).getNet().getListP()[3].getMark()
                    + model.getListObj().get(0).getNet().getListP()[6].getMark();
                    
                    totalSumOfStacked = totalSumOfStacked + model.getListObj().get(0).getNet().getListP()[2].getMark()
                    + model.getListObj().get(0).getNet().getListP()[4].getMark();
                    
                    
                    data[index] = new Object[]{createDelay[i], firstRegDelay[i], secondRegDelay[i], fullRegDelay[i], firstRegQ[i], secondRegQ[i], fullRegQ[i],
                        model.getListObj().get(0).getNet().getListP()[2].getObservedMax(),
                        model.getListObj().get(0).getNet().getListP()[2].getMean(),
                        model.getListObj().get(0).getNet().getListP()[2].getMark(),
                        model.getListObj().get(0).getNet().getListP()[4].getObservedMax(),
                        model.getListObj().get(0).getNet().getListP()[4].getMean(),
                        model.getListObj().get(0).getNet().getListP()[4].getMark(),
                        model.getListObj().get(0).getNet().getListP()[3].getMark(),
                        model.getListObj().get(0).getNet().getListP()[6].getMark(),
                        failureProbability
                    };
                    index++;
                    finProbs.add(failureProbability);
                    finProbsExp.add(failureProbability);
            }
            double total = 0;
                for (Double x : finProbsExp) {
                    total += x;
                }
                double avg = total / finProbsExp.size();
                for (int j = 0; j < numOfExp; j++) {
                    finProbsAvgGrouped.add(avg);
                }   
        }
        
        JTable table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920,1080);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        System.out.println();
        //System.out.println("Failure Probability: " + dFactual);
        //System.out.println("Size Of : " + degreesOfFreedom);
        
        double total = 0;
        for (Double x : finProbs) {
            total += x;
        }
        
        double failureProbability = total / finProbs.size();
        
              
        double stackProbability = (double) totalSumOfStacked / (totalSumOfStacked + totalSumOfProcessed)*100;
        double agregatsPerDay = (double) timeModeling / 30 * 2;
        long adviceForSize =  (long) ((Math.round(agregatsPerDay/100 * stackProbability)+5)/10)*10;
        
        System.out.println();
        System.out.println("Failure Probability: " + failureProbability);
        System.out.println("Stack Probability : " + stackProbability);
        System.out.println("Advice For Size : " + adviceForSize);
        
        
        
        for (int i = 0; i < finProbs.size(); i++) {
            groupDeviation.add(Math.pow((finProbs.get(i) - finProbsAvgGrouped.get(i)), 2));
        }
        ArrayList<Double> difference = new ArrayList<>();
        for (int i = 0; i < finProbs.size(); i++) {
            difference.add(Math.pow((finProbsAvgGrouped.get(i) - failureProbability), 2));
        }
        double sFactual = 0;
        for (Double x : difference) {
            sFactual += x;
        }
        sFactual *= numOfExp;
        double sResidual = 0;
        for (Double x : groupDeviation) {
            sResidual += x;
        }
        double degreesOfFreedom = numOfSituations * (numOfExp - 1);
        double dFactual = sFactual;
        double dResidual = sResidual / degreesOfFreedom;
        double f = dFactual / dResidual;
        double fCritical = 1.74635285;
        System.out.println();
        System.out.println("Sum of Squares: " + dFactual);
        System.out.println("Degrees of Freedom: " + degreesOfFreedom);
        System.out.println("Mean Square: " + dResidual);
        System.out.println("F: " + f);
        System.out.println("F-critical (alpha=0.05): " + fCritical);
        if (f > fCritical) {
            System.out.println("Factor is significant");
        } else {
            System.out.println("Factor is not significant");
        }
    }
    
    public static void validateModel(int timeModeling) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        PetriObjModel model = getModel(30, 30, 1, 30, 1, 100, 1);
        model.setIsProtokol(false);
        model.go(timeModeling);
        double practicalT1 = timeModeling / (double)(
                (model.getListObj().get(0).getNet().getListP()[3].getMark() + model.getListObj().get(0).getNet().getListP()[6].getMark()
                + model.getListObj().get(0).getNet().getListP()[2].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark())/2);        
        System.out.println("Theoretical T1 = 30");
        System.out.println("Practical T1 = " + practicalT1);
        System.out.println();

        double practicalT2 = timeModeling / (double)(model.getListObj().get(0).getNet().getListP()[2].getMark()
        + model.getListObj().get(0).getNet().getListP()[3].getMark());
        System.out.println("Theoretical T2 = 30");
        System.out.println("Practical T2 = " + practicalT2);
        System.out.println();
        
        double practicalT3 = timeModeling / (double)((model.getListObj().get(0).getNet().getListP()[3].getMark()));
        System.out.println("Theoretical T3 = 30");
        System.out.println("Practical T3 = " + practicalT3);
        System.out.println();
        
        double practicalT4 = timeModeling / (double)((model.getListObj().get(0).getNet().getListP()[6].getMark()));
        System.out.println("Theoretical T4 = 100");
        System.out.println("Practical T4 = " + practicalT4);
        System.out.println();
    }
    
    public static void findChebishev(int timeModeling) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        int runAmount = 100;
        
        ArrayList<Double> finProbs = new ArrayList<>();
        for (int i = 0; i < runAmount; i++) {
            PetriObjModel model = getModel(30, 30, 1, 30, 1, 100, 1);
            model.setIsProtokol(false);

            model.go(timeModeling);
            double failureProbability = model.getListObj().get(0).getNet().getListP()[3].getMark()
                    / (double)(model.getListObj().get(0).getNet().getListP()[3].getMark()
                    + model.getListObj().get(0).getNet().getListP()[6].getMark());
            finProbs.add(failureProbability);
        }
        double total = 0;
        double avg;
        for (Double finProb : finProbs) {
            total += finProb;
        }
        avg = total / finProbs.size();
        double sum = 0;
        for (Double finProb : finProbs) {
            sum += Math.pow((finProb - avg), 2);
        }
        double stdDev = Math.sqrt(sum / (finProbs.size()-1));
        double numOfExp = Math.pow(stdDev, 2) / (Math.pow((0.05 * avg), 2) * (1 - 0.95));
        System.out.println();
        System.out.println("===========================================");
        System.out.println();
        System.out.println("FINDING NUMBER OF EXPERIMENTS:");
        System.out.println();
        System.out.println("Average = " + avg);
        System.out.println("Standard Deviation = " + stdDev);
        System.out.println("Enough number of experiments: " + numOfExp);
    }
    
    public static PetriObjModel getModel(double createDelay, double firstRegDelay, int firstRegQ, double secondRegDelay, int secondRegQ, double fullRegDelay, int fullRegQ) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        list.add(new PetriSim(CreateAdjSectionNet(createDelay, firstRegDelay, firstRegQ, secondRegDelay, secondRegQ, fullRegDelay, fullRegQ)));

        return new PetriObjModel(list);
    }
    
    public static PetriNet CreateAdjSectionNet(double createDelay, double firstRegDelay, int firstRegQ, double secondRegDelay, int secondRegQ, double fullRegDelay, int fullRegQ) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("Генератор",1));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("Накопичувач",0));
	d_P.add(new PetriP("Агрегати з вторинного",0));
	d_P.add(new PetriP("Накопичувач",0));
	d_P.add(new PetriP("Первинне регулювання вільне",1));
	d_P.add(new PetriP("Агрегати з повного",0));
	d_P.add(new PetriP("Вторинне регулювання вільне",1));
	d_P.add(new PetriP("Повне регулювання вільне",1));
	d_T.add(new PetriT("Надходження",30.0));
	d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(0.0);
	d_T.add(new PetriT("Первинне регулювання",30.0));
	d_T.get(1).setDistribution("exp", d_T.get(1).getTimeServ());
	d_T.get(1).setParamDeviation(0.0);
	d_T.get(1).setPriority(10);
	d_T.add(new PetriT("Вторинне регулювання",30.0));
	d_T.get(2).setDistribution("exp", d_T.get(2).getTimeServ());
	d_T.get(2).setParamDeviation(0.0);
	d_T.add(new PetriT("Відмова",0.0));
	d_T.add(new PetriT("Повне регулювання",100.0));
	d_T.get(4).setDistribution("exp", d_T.get(4).getTimeServ());
	d_T.get(4).setParamDeviation(0.0);
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),2));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),2));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(3),2));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),2));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),2));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),2));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),2));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(8),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
	PetriNet d_Net = new PetriNet("coursework_v1.pns",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
    }
}


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
        int numOfExp = 6;
        
        firstExperiment(timeModeling);
        //showStatistics(numOfExp, timeModeling);
        //validateModel(timeModeling);
        //chebishevExperiment(timeModeling);
        //timeExperiment(numOfExp);
    }
    
    public static void firstExperiment(int timeModeling)throws
        ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        PetriObjModel model = getModel(30, 30, 1, 30, 1, 100, 1);
        model.setIsProtokol(false);
        model.go(timeModeling);
        double total = (double)(model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark()
        + model.getListObj().get(0).getNet().getListP()[3].getMark()) + model.getListObj().get(0).getNet().getListP()[2].getMark();
        double failureProbability = (model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark())/ total;
        double storageLoad = model.getListObj().get(0).getNet().getListP()[4].getMean()/total;
        
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~Solo Experiment~~~~~~~~~~~~~~~~~~~");
        System.out.println("MaxQ1: " + model.getListObj().get(0).getNet().getListP()[2].getObservedMax());
        System.out.println("MeanQ1: " + model.getListObj().get(0).getNet().getListP()[2].getMean());
        System.out.println("LeftQ1: " + model.getListObj().get(0).getNet().getListP()[2].getMark());
        System.out.println("MaxQ2: " + model.getListObj().get(0).getNet().getListP()[4].getObservedMax());
        System.out.println("MeanQ2: "  + model.getListObj().get(0).getNet().getListP()[4].getMean());
        System.out.println("LeftQ2: "  + model.getListObj().get(0).getNet().getListP()[4].getMark());
        System.out.println("NumOfFull: "  + model.getListObj().get(0).getNet().getListP()[6].getMark());
        System.out.println("NumOfSecond: "  + model.getListObj().get(0).getNet().getListP()[3].getMark());
        System.out.println("Failure Prob: "  + failureProbability);
        System.out.println("Storage Load: " + storageLoad);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public static void showStatistics(int numOfExp, int timeModeling) throws
        ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        JFrame frame = new JFrame();
        String[] columnNames = {"CreateDelay", "FirstRegDelay", "SecondRegDelay", "FullRegDelay", "MaxQ1" ,"MeanQ1" ,"LeftQ1" ,"MaxQ2" ,"MeanQ2" ,"LeftQ2", "NumOfSecond" ,"NumOfFull", "FProb", "SLoad"};
        double[] createDelay = {20.0, 30.0, 40.0};
        double[] firstRegDelay = {20.0, 30.0, 40.0}; 
        double[] secondRegDelay = {20.0, 30.0, 40.0}; 
        double[] fullRegDelay = {60.0, 100.0, 140.0};
        
        //double[] createDelay = {30.0, 30.0, 30.0};
        //double[] firstRegDelay = {30.0, 30.0, 30.0}; 
        //double[] secondRegDelay = {30.0, 30.0, 30.0}; 
        //double[] fullRegDelay = {100.0, 100.0, 100.0};
        int numOfSituations = 81;
        
        Object[][] data = new Object[numOfSituations*numOfExp][columnNames.length];
        int index = 0;
        
        ArrayList<Double> probabilities = new ArrayList<>();
        ArrayList<Double> groupDeviation = new ArrayList<>();
        ArrayList<Double> probabilitiesAvg = new ArrayList<>();
        ArrayList<Double> difference = new ArrayList<>();
        
        int totalSumOfProcessed = 0;
        int totalSumOfStucked = 0;
        int totalSumInStorage = 0;
        double totalSumInStorageMean = 0;
        
        for(int i1 = 0; i1 < createDelay.length; i1++){
            for(int i2 = 0; i2 < firstRegDelay.length; i2++){
                for(int i3 = 0; i3 < secondRegDelay.length; i3++){
                    for(int i4 = 0; i4 < fullRegDelay.length; i4++){
                        for(int j = 0; j < numOfExp; j++){
                            PetriObjModel model = getModel(createDelay[i1], firstRegDelay[i2], 1, secondRegDelay[i3], 1, fullRegDelay[i4], 1);
                            model.setIsProtokol(false);
                            model.go(timeModeling);
                            double total = ((double)(model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark()
                            + model.getListObj().get(0).getNet().getListP()[3].getMark()) + model.getListObj().get(0).getNet().getListP()[2].getMark());

                            double failureProbability = (model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark())
                            / total;
                            double storageLoad = model.getListObj().get(0).getNet().getListP()[4].getMean()/total;

                            totalSumOfProcessed = totalSumOfProcessed + model.getListObj().get(0).getNet().getListP()[3].getMark()+ model.getListObj().get(0).getNet().getListP()[6].getObservedMax();
                            totalSumOfStucked = totalSumOfStucked + model.getListObj().get(0).getNet().getListP()[2].getMark() + model.getListObj().get(0).getNet().getListP()[4].getObservedMax();
                            totalSumInStorage = totalSumInStorage + model.getListObj().get(0).getNet().getListP()[4].getObservedMax();
                            totalSumInStorageMean = totalSumInStorageMean + model.getListObj().get(0).getNet().getListP()[4].getMean();

                            data[index] = new Object[]{createDelay[i1], firstRegDelay[i2], secondRegDelay[i3], fullRegDelay[i4],
                                model.getListObj().get(0).getNet().getListP()[2].getObservedMax(),
                                model.getListObj().get(0).getNet().getListP()[2].getMean(),
                                model.getListObj().get(0).getNet().getListP()[2].getMark(),
                                model.getListObj().get(0).getNet().getListP()[4].getObservedMax(),
                                model.getListObj().get(0).getNet().getListP()[4].getMean(),
                                model.getListObj().get(0).getNet().getListP()[4].getMark(),
                                model.getListObj().get(0).getNet().getListP()[3].getMark(),
                                model.getListObj().get(0).getNet().getListP()[6].getMark(),
                                failureProbability, storageLoad
                            };
                            index++;
                            probabilities.add(failureProbability);
                        }
                        double total = probabilities.stream().mapToDouble(Double::doubleValue).sum();
                        double failureProbability = total / numOfExp;
                        for(int j = 0; j<numOfExp; j++){
                            probabilitiesAvg.add(failureProbability);
                        }
                    }
                }
            }
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920,1080);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        double total = probabilities.stream().mapToDouble(Double::doubleValue).sum();
        double failureProbability = total / probabilities.size();
        double storageLoad = (double) totalSumInStorageMean / (totalSumOfStucked + totalSumOfProcessed);
        double storageAvg = (double) totalSumInStorage / (numOfSituations * numOfExp);
        double stuckProbability = (double) totalSumOfStucked / (totalSumOfStucked + totalSumOfProcessed);
        long adviceForSize =  (long) ((Math.round(((totalSumOfStucked + totalSumOfProcessed)/(numOfSituations * numOfExp))* stuckProbability)+5)/10)*10;
        
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~Main Goals~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Failure Probability: " + failureProbability);
        System.out.println("Storage Load: " + storageLoad);
        System.out.println("Storage Avg Max: " + storageAvg);
        System.out.println("Stuck Probability : " + stuckProbability);
        System.out.println("Advice For Size : " + adviceForSize);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (int i = 0; i < probabilities.size(); i++) {
            groupDeviation.add(Math.pow((probabilities.get(i) - probabilitiesAvg.get(i)), 2));
            difference.add(Math.pow((probabilitiesAvg.get(i) - failureProbability), 2));
        }

        double sFactual = difference.stream().mapToDouble(Double::doubleValue).sum() * numOfExp;
        double sResidual = groupDeviation.stream().mapToDouble(Double::doubleValue).sum();
        double degreesOfFreedom = numOfSituations * (numOfExp - 1);
        double dFactual = sFactual;
        double dResidual = sResidual / degreesOfFreedom;
        double f = dFactual / dResidual;
        double fCritical = 1.41805107; //a=0.05; k1=49; k2=200
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~Analysis Of Variance~~~~~~~~~~~~~~~~");
        System.out.println("Sum of Squares: " + dFactual);
        System.out.println("Degrees of Freedom: " + degreesOfFreedom);
        System.out.println("Mean Square: " + dResidual);
        System.out.println("F: " + f);
        System.out.println("F-critical (alpha=0.05): " + fCritical);
        if (f > fCritical) {
            System.out.println("Influence is worthy of attention");
        } else {
            System.out.println("Influence is not worthy of attention");
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public static void validateModel(int timeModeling) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        PetriObjModel model = getModel(30, 30, 1, 30, 1, 100, 1);
        model.setIsProtokol(false);
        model.go(timeModeling);
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~Model Validation~~~~~~~~~~~~~~~~~~");
        double practicalT1 = timeModeling / (double)((model.getListObj().get(0).getNet().getListP()[3].getMark() + model.getListObj().get(0).getNet().getListP()[6].getMark()+ model.getListObj().get(0).getNet().getListP()[2].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark())/2);        
        System.out.println("Theoretical T1 = 30(exp); Factual T1 = " + practicalT1);

        double practicalT2 = timeModeling / (double)(model.getListObj().get(0).getNet().getListP()[2].getMark()+ model.getListObj().get(0).getNet().getListP()[3].getMark());
        System.out.println("Theoretical T2 = 30(exp); Factual T2 = "+ practicalT2);
        
        double practicalT3 = timeModeling / (double)((model.getListObj().get(0).getNet().getListP()[3].getMark()));
        System.out.println("Theoretical T3 = 30(exp); Factual T3 = " + practicalT3);
        
        double practicalT4 = timeModeling / (double)((model.getListObj().get(0).getNet().getListP()[6].getMark()));
        System.out.println("Theoretical T4 = 100(exp); Factual T4 = " + practicalT4);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public static void chebishevExperiment(int timeModeling) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        int runAmount = 100;
        
        ArrayList<Double> probabilities = new ArrayList<>();
        for (int i = 0; i < runAmount; i++) {
            PetriObjModel model = getModel(30, 30, 1, 30, 1, 100, 1);
            model.setIsProtokol(false);

            model.go(timeModeling);
            double failureProbability = (model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark())
                    / ((double)(model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark()
                    + model.getListObj().get(0).getNet().getListP()[3].getMark()) + model.getListObj().get(0).getNet().getListP()[2].getMark());
            probabilities.add(failureProbability);
        }
        double total = probabilities.stream().mapToDouble(Double::doubleValue).sum();
        double avg = total / probabilities.size();
        double sum = 0;
        for (Double finProb : probabilities) {
            sum += Math.pow((finProb - avg), 2);
        }
        double stdDev = Math.sqrt(sum / (probabilities.size()-1));
        double numOfExp = Math.pow(stdDev, 2) / (Math.pow((0.05 * avg), 2) * (1 - 0.95));
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~Chebyshev Test~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Failure Probability = " + avg);
        System.out.println("Standard Deviation = " + stdDev);
        System.out.println("Enough number of experiments by Chebishev: " + numOfExp);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public static void timeExperiment(int numOfExp) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        int timeModelingMax = 100000;
        int timeModelingStep = 100;
        int timeModelingStart = 100;
    
        JFrame frame = new JFrame();
        Object[][] data = new Object[(timeModelingMax-timeModelingStep)*numOfExp][2];
        String[] columnNames = {"CreateDelay", "FirstRegDelay","SecondRegDelay"};
        int index = 0;
        for (int i = 0; i < numOfExp; i++) {
            int t = timeModelingStart;
            while (t <= timeModelingMax) {
                PetriObjModel model = getModel(30, 30, 1, 30, 1, 100, 1);
                model.setIsProtokol(false);
                model.go(t);
                double total = (double)(model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark()
                    + model.getListObj().get(0).getNet().getListP()[3].getMark()) + model.getListObj().get(0).getNet().getListP()[2].getMark();
                double failureProbability = (model.getListObj().get(0).getNet().getListP()[6].getMark() + model.getListObj().get(0).getNet().getListP()[4].getMark())
                    / total;
                double storageLoad = model.getListObj().get(0).getNet().getListP()[4].getMean()/total;
                
                data[index] = new Object[]{t, failureProbability, storageLoad};
                
                t += timeModelingStep;
                index++;
            }
        }
        
        JTable table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920,1080);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
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
	d_P.add(new PetriP("Первинне регулювання вільне", firstRegQ ));
	d_P.add(new PetriP("Агрегати з повного",0));
	d_P.add(new PetriP("Вторинне регулювання вільне",secondRegQ));
	d_P.add(new PetriP("Повне регулювання вільне",fullRegQ));
	d_T.add(new PetriT("Надходження",createDelay));
	d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(0.0);
	d_T.add(new PetriT("Первинне регулювання",firstRegDelay));
	d_T.get(1).setDistribution("exp", d_T.get(1).getTimeServ());
	d_T.get(1).setParamDeviation(0.0);
	d_T.get(1).setPriority(10);
	d_T.add(new PetriT("Вторинне регулювання",secondRegDelay));
	d_T.get(2).setDistribution("exp", d_T.get(2).getTimeServ());
	d_T.get(2).setParamDeviation(0.0);
	d_T.add(new PetriT("Відмова",0.0));
	d_T.add(new PetriT("Повне регулювання", fullRegDelay));
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


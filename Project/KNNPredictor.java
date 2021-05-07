package projectthree;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KNNPredictor extends Predictor{
    private final int k;
    private int label0 = 0;
    private int label1 = 0;
    private ArrayList<DataPoint> dataPoints;
    private int trainSize = 0;
    private int testSize = 0;
    private ArrayList<DataPoint> fullDataPoints = new ArrayList<DataPoint>();
    private double truePositive = 0.0;
    private double falsePositive = 0.0;
    private double falseNegative = 0.0;
    private double trueNegative = 0.0;

    public KNNPredictor(int k) {
        this.k = k;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    @Override
    ArrayList<DataPoint> readData(String fileName) {
        ArrayList<DataPoint> dps = new ArrayList<DataPoint>();
        try (Scanner scanner = new Scanner(new File(fileName));) {
            List<String> records;
            while (scanner.hasNextLine()) {
                records = this.getRecordFromLine(scanner.nextLine());
                // Select the columns from the records and create a DataPoint object
                // age and fare. Do not use data without age or fare
                if (records.size() == 7 && !records.get(5).equals("")) {
                    String label = records.get(1);
                    if (label.equals("0")) {
                        label0++;
                    } else {
                        label1++;
                    }

                    Double f1 = Double.parseDouble(records.get(5));
                    Double f2 = Double.parseDouble(records.get(6));

                    Random rand = new Random();
                    double randNum = rand.nextDouble();
                    boolean isTest;

                    if (randNum < 0.9) {
                        isTest = false;
                        trainSize++;
                    }else {
                        isTest = true;
                        testSize++;
                    }

                    DataPoint dp = new DataPoint(f1,f2,label,isTest);
                    // Store the DataPoint object in a collection
                    dps.add(dp);
                    fullDataPoints.add(new DataPoint(f1,f2,label,false));
                }
            }

        } catch (IOException e){e.printStackTrace();}
        System.out.println("Death: " + String.valueOf(label0));
        System.out.println("Survive: " + String.valueOf(label1));
        this.dataPoints =dps;
        return dps;
    }

    private double getDistance(DataPoint p1, DataPoint p2) {
        double f1_p1 = p1.getF1();
        double f2_p1 = p1.getF2();
        double f1_p2 = p2.getF1();
        double f2_p2 = p2.getF2();
        double result = Math.sqrt(Math.pow(f1_p1 - f1_p2,2) + Math.pow(f2_p1-f2_p2,2));
        return result;
    }

    @Override
    String test(DataPoint data) {
        Double[][] distanceAndLabel = new Double[trainSize][2];
        int trainCount = 0;
        if (data.getTest()) {
            for (DataPoint dp: dataPoints) {
                if(!dp.getTest()) {
                    distanceAndLabel[trainCount][1] = Double.parseDouble(dp.getLabel());
                    distanceAndLabel[trainCount][0] = getDistance(data,dp);
                    trainCount++;
                }
            }
        }

        Arrays.sort(distanceAndLabel, new Comparator<Double[]>() {
            public int compare(Double[] a, Double [] b) {
                return a[0].compareTo(b[0]);
            }
        });

        int count = 0;
        int count0 = 0;
        int count1 = 0;
        while (count < k) {
            if (distanceAndLabel[count][1] == 0.0) {
                count0++;
            } else {
                count1++;
            }
            count++;
        }

        if (count0 > count1) {
            return "0";
        } else {
            return "1";
        }
    }

    @Override
    Double getAccuracy(ArrayList<DataPoint> data) {
        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint dpTest = data.get(i);
            if (dpTest.getTest()) {
                String labelTest = test(dpTest);
                String labelActual = fullDataPoints.get(i).getLabel();

                if (labelTest.equals("1") && labelActual.equals("1")) {
                    truePositive++;
                }
                else if (labelTest.equals("1") && labelActual.equals("0")) {
                    falsePositive++;
                }
                else if (labelTest.equals("0") && labelActual.equals("1")) {
                    falseNegative++;
                }
                else {
                    trueNegative++;
                }
            }
        }
        return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
    }

    @Override
    Double getPrecision(ArrayList<DataPoint> data) {
        return truePositive / (truePositive + falseNegative);
    }
}

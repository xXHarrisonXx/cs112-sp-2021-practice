package projectfour;

import java.util.*;

public class KNNModel {
    private int k = 5;
    private List<DataPoint> testData;
    private List<DataPoint> trainData;
    private int trainSize;

    private double truePositive = 0.0;
    private double falsePositive = 0.0;
    private double falseNegative = 0.0;
    private double trueNegative = 0.0;

    public KNNModel() {
    }

    public KNNModel(int k) {
        this.k = k;
    }


    public void readData(List<DataPoint> testData, List<DataPoint> trainData) {
        this.trainData = trainData;
        this.testData = testData;
        this.trainSize = trainData.size();

    }



    private double getDistance(DataPoint p1, DataPoint p2) {
        double f1_p1 = p1.getF1();
        double f2_p1 = p1.getF2();
        double f1_p2 = p2.getF1();
        double f2_p2 = p2.getF2();
        double result = Math.sqrt(Math.pow(f1_p1 - f1_p2,2) + Math.pow(f2_p1-f2_p2,2));
        return result;
    }


    public String test(DataPoint data) {
        Double[][] distanceAndLabel = new Double[trainSize][2];
        int trainCount = 0;
        for (DataPoint dp: trainData) {
            distanceAndLabel[trainCount][1] = Double.parseDouble(dp.getLabel());
            distanceAndLabel[trainCount][0] = getDistance(data,dp);
            trainCount++;

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


    public void train() {

        for (int i = 0; i < testData.size(); i++) {
            DataPoint dpTest = testData.get(i);
            String labelTest = test(dpTest);
            String labelActual = testData.get(i).getLabel();

            if (labelTest.equals("1") && labelActual.equals("1")) {
                testData.get(i).setType("truePositive");
                truePositive++;
            }
            else if (labelTest.equals("1") && labelActual.equals("0")) {
                testData.get(i).setType("falsePositive");
                falsePositive++;
            }
            else if (labelTest.equals("0") && labelActual.equals("1")) {
                testData.get(i).setType("falseNegative");
                falseNegative++;
            }
            else {
                testData.get(i).setType("trueNegative");
                trueNegative++;
            }

        }
    }


    public Double getAccuracy() {
        return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
    }

    public Double getPrecision() {
        return truePositive / (truePositive + falseNegative);
    }


    public void setK(int k) {
        this.k = k;
    }


}


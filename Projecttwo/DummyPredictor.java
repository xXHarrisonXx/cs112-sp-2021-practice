package projecttwo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DummyPredictor extends Predictor {
    private double avgTest;
    private double avgTraining;

    @Override
    ArrayList<DataPoint> readData(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            ArrayList<DataPoint> dataPoints = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] strAry = line.split(" ");
                DataPoint dataPoint = new DataPoint(Double.valueOf(strAry[0]),
                        Double.valueOf(strAry[1]),
                        strAry[2],
                        Boolean.valueOf(strAry[3]));
                dataPoints.add(dataPoint);
            }
            setAvg(dataPoints);
            return dataPoints;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    String test(DataPoint data) {
        double diff = Math.abs(data.getF1() - data.getF2());
        double a = Math.abs(diff - this.avgTest);
        double b = Math.abs(diff - this.avgTraining);
        if(a < b) {
        	return "Test";
        }else {
        	return "Training";
        }
    }

    @Override
    Double getAccuracy(ArrayList<DataPoint> data) {
        return this.avgTest;
    }

    @Override
    Double getPrecision(ArrayList<DataPoint> data) {
        return this.avgTraining;
    }

    private void setAvg(ArrayList<DataPoint> dataPoints) {
        int countTest = 0;
        int countTraining = 0;
        double totalTestDiff = 0.0;
        double totalTrainingDiff = 0.0;
        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint dataPoint = dataPoints.get(i);
            double diff = Math.abs(dataPoint.getF1() - dataPoint.getF2());
            if (dataPoint.getTest()) {
                countTest++;
                totalTestDiff = totalTestDiff + diff;
            } else {
                countTraining++;
                totalTrainingDiff = totalTrainingDiff + diff;
            }
        }
        System.out.println(countTest + " " + totalTestDiff);
        System.out.println(countTraining + " " + totalTrainingDiff);
        this.avgTest = totalTestDiff / countTest;
        this.avgTraining = totalTrainingDiff / countTraining;
    }
}

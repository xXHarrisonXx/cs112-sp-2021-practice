package projecttwo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Driver {


    private void initAndShowGUI(Double accuracy, Double precision) {
        JFrame myFrame = new JFrame();
        myFrame.setTitle("Project Two");
        Container contentPane = myFrame.getContentPane();
        contentPane.setLayout(new GridLayout(5, 5));
        contentPane.add(new Label("k-value: "));

        JTextField k_value = new JTextField();
        contentPane.add(new JLabel("click to change K value: "));
        JButton resetK = new JButton("reset");


        contentPane.add(k_value);
        contentPane.add(resetK);

        contentPane.add(new Label("the accuracy: "));
        String acc = new DecimalFormat("##.##").format(accuracy);
        Label accL = new Label(acc);
        contentPane.add(accL);
        String pre = new DecimalFormat("##.##").format(precision);
        Label preL = new Label(pre);
        contentPane.add(new Label("the precision: "));
        contentPane.add(preL);
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        resetK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KNNPredictor knnPredictor = new KNNPredictor(Integer.parseInt(k_value.getText()));
                String fileName = "titanic.csv";
                ArrayList<DataPoint> dps = knnPredictor.readData(fileName);
                double accuracy0 = knnPredictor.getAccuracy(dps);
                double precision0 = knnPredictor.getPrecision(dps);
                String acc = new DecimalFormat("##.##").format(accuracy0);
                String pre = new DecimalFormat("##.##").format(precision0);
                accL.setText(acc);
                preL.setText(pre);

            }
        });
    }


    public static void main(String args[]) {

        Driver driver = new Driver();
        KNNPredictor knnPredictor = new KNNPredictor(5);
        String fileName = "titanic.csv";

        ArrayList<DataPoint> dps = knnPredictor.readData(fileName);

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        driver.initAndShowGUI(knnPredictor.getAccuracy(dps), knnPredictor.getPrecision(dps));
                    }
                }
        );
    }
}

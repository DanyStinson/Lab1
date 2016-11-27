package com;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GUI gui = new GUI();

        JFrame frame = new JFrame("ICO LAB GR-08");
        frame.setContentPane(gui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(935, 720);
    }
}

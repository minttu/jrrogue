package me.pieso.jrrogue;

import java.io.IOException;
import javax.swing.SwingUtilities;
import me.pieso.jrrogue.GUI.GUI;

public class Main {

    public static void main(String[] args) throws IOException {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(gui);
    }

}

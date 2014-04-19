package me.pieso.jrrogue;

import me.pieso.jrrogue.GUI.GUI;
import java.io.IOException;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) throws IOException {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(gui);
    }

}

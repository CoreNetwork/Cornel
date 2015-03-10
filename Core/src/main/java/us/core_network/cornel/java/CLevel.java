package us.core_network.cornel.java;

import java.util.logging.Level;

public class CLevel extends Level {
    public static final Level POSITIVE = new CLevel("POSITIVE", 850); // level for success messages, slightly above INFO

    protected CLevel(String name, int value) {
        super(name, value);
    }
}

package us.core_network.cornel.java;

public class CLevel extends java.util.logging.Level {
    protected CLevel(String name, int value) {
        super(name, value);
    }

    static {
        CLevel.WARNING;
    }
}

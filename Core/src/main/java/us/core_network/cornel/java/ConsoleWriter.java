package us.core_network.cornel.java;

import org.bukkit.command.ConsoleCommandSender;

import java.io.IOException;
import java.io.Writer;

public class ConsoleWriter extends Writer {
    private ConsoleCommandSender console;
    private StringBuilder buffer = new StringBuilder();
    private StringBuilder begunLine = new StringBuilder();
    private String prefix;

    public ConsoleWriter(ConsoleCommandSender console, String prefix) {
        this.console = console;
        this.prefix = prefix;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        buffer.append(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        String current = buffer.toString();
        buffer.setLength(0);
        String[] lines = current.split("\n");
        begunLine.append(lines[0]);
        console.sendMessage(prefix + begunLine.toString());
        begunLine.setLength(0);
        for (int i = 1; i < lines.length; i++) {
            if (i == lines.length - 1) {
                if (!lines[i].endsWith("\n")) {
                    begunLine.append(lines[i]);
                    continue;
                }
            }
            console.sendMessage(prefix + lines[i]);
        }
    }

    @Override
    public void close() throws IOException {

    }
}

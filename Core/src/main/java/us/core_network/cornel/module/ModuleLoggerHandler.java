package us.core_network.cornel.module;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import us.core_network.cornel.java.CLevel;
import us.core_network.cornel.java.ConsoleWriter;

import java.io.PrintWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ModuleLoggerHandler extends Handler {
    private final Module module;
    private ConsoleCommandSender console;
    private PrintWriter errorWriter;

    public ModuleLoggerHandler(Module module) {
        this.module = module;
        console = Bukkit.getConsoleSender();
        errorWriter = new PrintWriter(new ConsoleWriter(console, "\t\t"));
    }

    @Override
    public void publish(LogRecord record) {
        StringBuilder message = new StringBuilder();
        message.append(generatePrefix(getModule().getPlugin().getName() + "|" + getModule().getName(), record.getLevel()))
                .append(record.getMessage());
        console.sendMessage(message.toString());
        if (record.getThrown() != null) {
            record.getThrown().printStackTrace(errorWriter);
            errorWriter.flush();
        }
    }

    public static String generatePrefix(String content, Level level) {
        ChatColor color = getChatColor(level);
        boolean bleed = level.intValue() >= Level.WARNING.intValue() || level == CLevel.POSITIVE;
        StringBuilder message = new StringBuilder();
        message.append(color)
                .append('[');
        if (!bleed) {
            message.append(ChatColor.RESET);
        }
        message.append(content)
                .append(color)
                .append(']')
                .append(bleed ? ChatColor.WHITE : ChatColor.RESET)
                .append(": ");
        return message.toString();
    }

    private static ChatColor getChatColor(Level level) {
        if (level.intValue() < Level.INFO.intValue()) {
            return ChatColor.DARK_AQUA;
        }
        if (level == Level.INFO) {
            return ChatColor.RESET;
        }
        if (level == Level.WARNING) {
            return ChatColor.GOLD;
        }
        if (level == Level.SEVERE) {
            return ChatColor.DARK_RED;
        }
        if (level == CLevel.POSITIVE) {
            return ChatColor.GREEN;
        }
        return ChatColor.GRAY;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}

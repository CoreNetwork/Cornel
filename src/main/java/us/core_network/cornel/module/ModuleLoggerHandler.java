package us.core_network.cornel.module;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
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
        errorWriter = new PrintWriter(new ConsoleWriter(console, generatePrefix(module.getName(), Level.SEVERE)));
    }

    @Override
    public void publish(LogRecord record) {
        StringBuilder message = new StringBuilder();
        message.append(generatePrefix(getModule().getName(), record.getLevel()))
                .append(record.getMessage());
        console.sendMessage(message.toString());
        if (record.getThrown() != null) {
            record.getThrown().printStackTrace(errorWriter);
        }
    }

    public static String generatePrefix(String content, Level level) {
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.WHITE)
                .append('[')
                .append(getChatColor(level))
                .append(content)
                .append(ChatColor.WHITE)
                .append("]: ");
        return message.toString();
    }

    private static ChatColor getChatColor(Level level) {
        if (level.intValue() < Level.INFO.intValue()) {
            return ChatColor.GREEN;
        }
        if (level == Level.INFO) {
            return ChatColor.WHITE;
        }
        if (level == Level.WARNING) {
            return ChatColor.GOLD;
        }
        if (level == Level.SEVERE) {
            return ChatColor.DARK_RED;
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

package us.core_network.cornel.module;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ModuleBukkitManager {
    private PluginManager pm;
    private BukkitScheduler scheduler;
    private Module module;
    private LinkedHashSet<Integer> tasks = new LinkedHashSet<>();
    private Set<EventRecord> registeredListeners = new HashSet<>();

    public ModuleBukkitManager(Module module) {
        this.module = module;
        pm = Bukkit.getPluginManager();
        scheduler = Bukkit.getScheduler();
    }

    public void removeAll() {
        cancelAllTasks();

        registeredListeners.forEach((record) -> {
            try {
                Method getHandlerList = record.eventClass.getMethod("getHandlerList");
                HandlerList list = (HandlerList) getHandlerList.invoke(null);
                list.unregister(record.listener);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public int scheduleSyncDelayedTask(Runnable task, long delay) {
        int id = scheduler.scheduleSyncDelayedTask(module.getPlugin(), task, delay);
        tasks.add(id);
        return id;
    }

    
    public int scheduleSyncDelayedTask(Runnable task) {
        int id = scheduler.scheduleSyncDelayedTask(module.getPlugin(), task);
        tasks.add(id);
        return id;
    }

    
    public int scheduleSyncRepeatingTask(Runnable task, long delay, long interval) {
        int id = scheduler.scheduleSyncRepeatingTask(module.getPlugin(), task, delay, interval);
        tasks.add(id);
        return id;
    }
    
    public int scheduleAsyncDelayedTask(Runnable task, long delay) {
        int id = scheduler.scheduleSyncDelayedTask(module.getPlugin(), task, delay);
        tasks.add(id);
        return id;
    }

    
    public int scheduleAsyncDelayedTask(Runnable task) {
        int id = scheduler.scheduleAsyncDelayedTask(module.getPlugin(), task);
        tasks.add(id);
        return id;
    }

    
    public void cancelTask(int taskId) {
        if (tasks.contains(taskId)) {
            scheduler.cancelTask(taskId);
            tasks.remove(taskId);
        }
    }

    
    public void cancelAllTasks() {
        for (Integer id : tasks) {
            scheduler.cancelTask(id);
        }
        tasks.clear();
    }

    
    public boolean isCurrentlyRunning(int taskId) {
        return scheduler.isCurrentlyRunning(taskId);
    }

    
    public boolean isQueued(int taskId) {
        return scheduler.isQueued(taskId);
    }

    
    public List<BukkitWorker> getActiveWorkers() {
        throw new UnsupportedOperationException();
    }

    
    public List<BukkitTask> getPendingTasks() {
        throw new UnsupportedOperationException();
    }

    
    public BukkitTask runTask(Runnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public BukkitTask runTaskAsynchronously(Runnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public BukkitTask runTaskLater(Runnable task, long l) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }


    public BukkitTask runTaskLaterAsynchronously(Runnable task, long l) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public BukkitTask runTaskTimer(Runnable task, long l, long l1) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public BukkitTask runTaskTimerAsynchronously(Runnable task, long l, long l1) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void registerEvents(Listener listener) {
        pm.registerEvents(listener, module.getPlugin());

        Arrays.asList(listener.getClass().getMethods())
                .stream()
                .filter(m -> m.getAnnotation(EventHandler.class) != null)
                .forEach(m -> {
                    Parameter p[] = m.getParameters();
                    if (p.length == 1) {
                        Class<?> clazz = p[0].getType();
                        if (Event.class.isAssignableFrom(clazz)) {
                            addListeners((Class<? extends Event>) clazz, listener);
                        }
                    }
                });
    }

    private void addListeners(Class<? extends Event> event, Listener listener) {
        try {
            Method handlerList = event.getMethod("getHandlerList");
            handlerList.setAccessible(true);
            HandlerList list = (HandlerList) handlerList.invoke(null);
            Arrays.asList(list.getRegisteredListeners())
                    .stream()
                    .filter(l -> l.getListener() == listener)
                    .forEach((l2) -> {
                        registeredListeners.add(new EventRecord(l2, event));
                    });
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    
    public void registerEvent(Class<? extends Event> aClass, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor) {
        pm.registerEvent(aClass, listener, eventPriority, eventExecutor, module.getPlugin());
        addListeners(aClass, listener);
    }

    
    public void registerEvent(Class<? extends Event> aClass, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, boolean b) {
        pm.registerEvent(aClass, listener, eventPriority, eventExecutor, module.getPlugin(), b);
        addListeners(aClass, listener);
    }
    
    public boolean useTimings() {
        throw new UnsupportedOperationException();
    }

    private static class EventRecord {
        RegisteredListener listener;

        public EventRecord(RegisteredListener listener, Class<? extends Event> eventClass) {
            this.listener = listener;
            this.eventClass = eventClass;
        }

        Class<? extends Event> eventClass;
    }
}

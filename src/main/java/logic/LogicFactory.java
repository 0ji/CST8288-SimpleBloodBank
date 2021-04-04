package logic;

import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.juli.logging.Log;

public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    public static < T> T getFor(String entityName) {
        T newInstance=null;
        try {
            Class<T> type = (Class<T>) (Class.forName(PACKAGE + entityName + SUFFIX));
            newInstance = getFor(type);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LogicFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newInstance;
    }

    public static < T> T getFor(Class<T> type) {
         T newInstance=null;
        try {
            Constructor<T> declaredConstructor = type.getDeclaredConstructor();
            newInstance = declaredConstructor.newInstance();
            
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(LogicFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newInstance;
    }
}

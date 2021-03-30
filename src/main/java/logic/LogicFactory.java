package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
        
public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    public static < T> T getFor( String entityName ) {
        
        try{
        Class<T> type= (Class<T>)(Class.forName(PACKAGE + entityName + SUFFIX));
        T newInstance =getFor(type);
        return newInstance;
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }  
        return null;
    }
    
    public static < T> T getFor( Class<T> type) {
        try{
        Constructor<T> declaredConstructor= type.getDeclaredConstructor();
        T newInstance= declaredConstructor.newInstance();
        return newInstance;
        }catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
        InvocationTargetException | NoSuchMethodException | SecurityException ex){
            ex.printStackTrace();
        }
       return null;
    }
}

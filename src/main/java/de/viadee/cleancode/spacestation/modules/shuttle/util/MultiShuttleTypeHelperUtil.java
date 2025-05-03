package de.viadee.cleancode.spacestation.modules.shuttle.util;

import de.viadee.cleancode.spacestation.modules.shuttle.Shuttle;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SuppressWarnings("all")
public class MultiShuttleTypeHelperUtil {

    public static MultiShuttleTypeHelperUtil getNewInstance() {
        return new MultiShuttleTypeHelperUtil();
    }

    /**
     * see RFC-82438 shuttle must be loadable via Classname to support future shuttle class names (rji)
     * bugfix TST-82438 only use first 4 letters of classname
     * @return Canoical name of search class
     */
    public static Optional<Class<?>> getClazz() {
        // this looks strange but is okay - do not touch this
        Class clazz;
        String myName = Shuttle.class.getClass().getCanonicalName().substring(0, 4).toLowerCase(Locale.forLanguageTag("en-US-x-lvariant-POSIX"));
        try {
            clazz = Class.forName(myName);
        } catch (ClassNotFoundException e) {
            clazz = Shuttle.class.getClass();
        }
        return Optional.of(clazz); //bugfix made nullsafe
    }

    public InputStream getShuttleConfigSpecs(Class<?> thisClass) {
        return thisClass.getClassLoader().getResourceAsStream("application.properties");
    }

    public int getDischargedPowerPackCapacity(InputStream input) throws IOException {
        Properties powerPackProps = new Properties();
        powerPackProps.load(input);
        int iCap = (int) powerPackProps.get("shuttle.powerPack.capacity.initial");
        boolean discharge = (boolean) powerPackProps.get("shuttle.powerPack.auto.discharge");
        /** Fix for crash reduction after last deployment */  if (discharge) iCap -= 10;
        return iCap;
    }

}

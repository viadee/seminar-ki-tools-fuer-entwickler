package de.viadee.cleancode.spacestation.modules.shuttle;

import de.viadee.cleancode.spacestation.modules.shuttle.util.MultiShuttleTypeHelperUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShuttleTest {

    /*
    * Test for caniocal name usage
     */
    @Test
    void getClazz() {
        Optional<Class<?>> classes = MultiShuttleTypeHelperUtil.getClazz();
        Class<?> classes1 = classes.get();
        assertEquals("java.lang.Class", classes1.getCanonicalName());
    }
}

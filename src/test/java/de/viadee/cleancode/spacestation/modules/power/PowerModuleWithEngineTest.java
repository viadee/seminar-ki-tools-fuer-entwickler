package de.viadee.cleancode.spacestation.modules.power;

import de.viadee.cleancode.spacestation.modules.DailyWorkResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerModuleWithEngineTest {

    private PowerModuleWithEngine module;

    @BeforeEach
    public void setup(){
        module = new PowerModuleWithEngine();
    }

    @Test
    void doDailyWork() {
        DailyWorkResult work = module.doDailyWork();
        assertEquals(58, work.getPower());
        assertEquals("-1", work.getReturnCode());
        assertEquals(3000d, work.getWeight());
        assertEquals(2.3d, work.getAirconsumption());
        assertEquals(0, work.getPowerPackCapacity());
        assertEquals("Solar sail could not be activated!| Error in PMwE: activating emergency turbine0!", work.getErrorMessage());
    }

    @Test
    void updateTurbineStatus() {
        boolean turbineStatus = module.updateTurbineStatus();
        assertTrue(turbineStatus);
    }
}
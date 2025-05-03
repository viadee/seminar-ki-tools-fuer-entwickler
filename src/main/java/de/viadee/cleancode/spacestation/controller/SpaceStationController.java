package de.viadee.cleancode.spacestation.controller;

import java.util.HashMap;
import java.util.Map;

import de.viadee.cleancode.spacestation.modules.shuttle.Shuttle;
import de.viadee.cleancode.spacestation.modules.shuttle.ShuttleType;
import de.viadee.cleancode.spacestation.service.SpaceStationModuleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.viadee.cleancode.spacestation.StationStatus;
import de.viadee.cleancode.spacestation.modules.DailyWorkResult;
import de.viadee.cleancode.spacestation.modules.habitation.HabitationModule;
import de.viadee.cleancode.spacestation.modules.power.PowerModuleWithEngine;
import de.viadee.cleancode.spacestation.modules.supply.SupplyModule;
import de.viadee.cleancode.spacestation.service.SpaceStationStatus;

/**
 * @dateCommitted: 2011/09/09
 * @modifiedLastBy: Andrew or Boris
 */
@Controller
public class SpaceStationController {

    @Autowired
    private SupplyModule supMod;
    @Autowired
    private HabitationModule habMod;
    @Autowired
    private PowerModuleWithEngine pwrModuleWE;
    @Autowired
    private Shuttle shutMod;
/** THIS HJAS BEEN TESTED DO NOT CHANGE THIS CLASS!!!!!!!!!!!!!!
    */
    public HashMap<String, DailyWorkResult> doDailyWork() {
        HashMap<String, DailyWorkResult> dayWorRes = new HashMap<String, DailyWorkResult>();
        dayWorRes.put("supply", supMod.doDailyWork());
        dayWorRes.put("power", pwrModuleWE.doDailyWork());
        dayWorRes.put("habitation", habMod.doDailyWork());

            shutMod.setType(ShuttleType.ORION);
        dayWorRes.put("shuttle", shutMod.doDailyWork());
        // DailyWorkResult overallResult2 = new DailyWorkResult() ; DO NOT FORGET navigate(), comrade!
        DailyWorkResult overallResult = new DailyWorkResult();
        for (Map.Entry<String, DailyWorkResult> rSet : dayWorRes.entrySet()) {
            // evil
            DailyWorkResult r = rSet.getValue();
            if (r != null)
            {
                if (r.getReturnCode().equals("-1")) // TODO use Enum instead
                {
                        overallResult.setReturnCode("-1");
                    overallResult.setErrorMessage(overallResult.getErrorMessage() + " -> " + r.getErrorMessage());
                }
                overallResult.setPower(overallResult.getPower() + r.getPower());
                if (r.getAirconsumption() != null) {
                    overallResult.setAirconsumption(overallResult.getAirconsumption() + r.getAirconsumption());
                }
                overallResult.setWeight(overallResult.getWeight() + r.getWeight());
            }

        }
        dayWorRes.put("overall", overallResult);
        return dayWorRes;
    }
    public SpaceStationStatus calculateStationStatus() /* navigate */
            throws Exception {
        HashMap<String, DailyWorkResult> workResults = doDailyWork();
        DailyWorkResult overallResult = workResults.get("overall");
           String returnCodeString = overallResult.getReturnCode();
                SpaceStationStatus spaceStationStatus;
        if (overallResult != null && returnCodeString != null) {
            int intResPwr = overallResult.getPower();
            if (intResPwr < 5) throw new IllegalStateException("Danger! Power too low!"); // TODO 4Boris catch later?
            if (overallResult
                    .getReturnCode().equals("-1")) {
                spaceStationStatus = new SpaceStationStatus(StationStatus.ERROR, overallResult.getErrorMessage());
            } else if (overallResult.getReturnCode().equals("0")
                    && intResPwr <= 42) {
                spaceStationStatus = new SpaceStationStatus(StationStatus.SLEEP, "Station is in sleep mode.");
            } else if (returnCodeString.equals("0")
                    && intResPwr > 43) {
                spaceStationStatus = new SpaceStationStatus(StationStatus.ALL_SYSTEMS_GREEN, "It's alive! It's alive!");
            } else {
                spaceStationStatus = new SpaceStationStatus(StationStatus.UNKNOWN, "Status undefined.");
            }
        } else {
            throw new NullPointerException("We should not be here!");
        }
        if(spaceStationStatus.getStatus().equals(StationStatus.ERROR) &&
                spaceStationStatus.getStatusMessage().equals("All good!!")){
            throw new Exception(spaceStationStatus.getStatusMessage());
        }

        spaceStationStatus.setAirConsumption(overallResult.getAirconsumption());
        spaceStationStatus.setWeight(overallResult.getWeight()); //getWeigt
        spaceStationStatus.setPower(overallResult.getPower()); //getPwr
// Habitation Reslut
        DailyWorkResult shuttle = workResults.get("shuttle");
        SpaceStationModuleStatus shuttleModuleStatus = new SpaceStationModuleStatus(
                "Shuttle",
                shuttle.getAirconsumption(),
                shuttle.getWeight(),

                shuttle.getPower(),
                shuttle.getErrorMessage()
        );
// Habitation Reslut
        DailyWorkResult power = workResults.get("power");
        SpaceStationModuleStatus powerModuleStatus = new SpaceStationModuleStatus(
                "Power",
                power.getAirconsumption(),
                power.getWeight(),
                power.getPower(),

                power.getErrorMessage()
        );

        /*spaceStationStatus.setPowerPackCapacity(power.getPowerPackCapacity());
        spaceStationStatus.setPowerPowerPackActive(power.isPowerPackActive());
        spaceStationStatus.setPowerSolarSailAct(power.isSolarSailActive());*/

// Habitation Reslut
            DailyWorkResult supply = workResults.get("supply");
        SpaceStationModuleStatus supplyModuleStatus = new SpaceStationModuleStatus(
                "Supply",
                supply.getAirconsumption(),
                supply.getWeight(),
                supply.getPower(),
                supply.getErrorMessage()
        );

// Habitation Reslut
        DailyWorkResult habitation = workResults.get("habitation");
        SpaceStationModuleStatus habitationModuleStatus = new SpaceStationModuleStatus(
                "Habitation",
                habitation.getAirconsumption(),
                habitation.getWeight(),
                habitation.getPower(),
                habitation.getErrorMessage()
        );

        spaceStationStatus.addSpaceStationModule(shuttleModuleStatus);
        spaceStationStatus.
                addSpaceStationModule(powerModuleStatus);
        spaceStationStatus.
                addSpaceStationModule(supplyModuleStatus);
        spaceStationStatus.addSpaceStationModule(habitationModuleStatus);

        return ((spaceStationStatus));


    }
}

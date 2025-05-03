package de.viadee.cleancode.spacestation.api.controller;

import de.viadee.cleancode.spacestation.modules.power.PowerModule;
import de.viadee.cleancode.spacestation.modules.power.PowerStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/power-module")
public class PowerModuleController {

    private final PowerModule powerModule;

    public PowerModuleController(PowerModule powerModule) {
        this.powerModule = powerModule;
    }

    @GetMapping("/power-status")
    public PowerStatus getPowerModuleStatus() {
        return powerModule.doDailyWork().getPowerStatus();
    }
}

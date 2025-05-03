package de.viadee.cleancode.spacestation.modules.shuttle;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.viadee.cleancode.spacestation.modules.shuttle.util.MultiShuttleTypeHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import de.viadee.cleancode.spacestation.modules.DailyWorkResult;
import de.viadee.cleancode.spacestation.modules.Module;
import de.viadee.cleancode.spacestation.modules.power.Destination;
import de.viadee.cleancode.spacestation.modules.power.PowerModule;
import de.viadee.cleancode.spacestation.modules.supply.Coffee;
import de.viadee.cleancode.spacestation.modules.supply.CoffeeType;
import de.viadee.cleancode.spacestation.modules.supply.PizzaType;
import de.viadee.cleancode.spacestation.modules.supply.SupplyModule;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class Shuttle implements Module {

	boolean powerPackActive = false;
	
	@Autowired
	private PowerModule powerModule;
	
	@Autowired
	private SupplyModule supplyModule;

	@Value("${shuttle.powerPack.capacity.initial}")
	int powerPackCapacity;

	@Value("${shuttle.consumption.air}")
	Integer airConsumption;

	@Value("${shuttle.consumption.power}")
	Integer powerConsumption;

	@Value("${shuttle.weight}")
	Double weight;

	@Value("${shuttle.isConnected}")
	boolean isConnected;

	private ShuttleType type;

	private static final SecureRandom random = new SecureRandom();


	@Override
	public Double getWeight() {
		return weight;
	}

	@Override
	public int getPowerConsumption() {
		switch (type) {
		case SOJUS:
			powerConsumption = (int) Math.round(weight) * getNumberOfAstronauts() * 2;
			break;
		case ORION:
			powerConsumption = (int) Math.round(weight) * getNumberOfAstronauts() * 6;
			break;
		case SPACE_X:
			powerConsumption = (int) Math.round(weight) * getNumberOfAstronauts() * 3;
			break;
		default:
			throw new RuntimeException("Unknown type of shuttle!");
		}
		return powerConsumption;
	}
	
	public int getNumberOfAstronauts() {
		int numberOfAstronauts;
		switch (type) {
		case SOJUS:
			if (weight < 1500) {
				numberOfAstronauts = 1;
			}
			else numberOfAstronauts = 2;
			break;
		case ORION:
			if (weight < 1500) {
				numberOfAstronauts = 5;
			}
			else numberOfAstronauts = 7;
			break;
		case SPACE_X:
			if (weight < 1500) {
				numberOfAstronauts = 4;
			}
			else numberOfAstronauts = 6;
			break;
		default:
			throw new RuntimeException("Unknown number of astronauts!");
		}
		return numberOfAstronauts;
	}

	@Override
	public Double getAirConsumption() {
		return Double.valueOf(airConsumption);
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public DailyWorkResult doDailyWork() {
		DailyWorkResult result = new DailyWorkResult();
		result = getSupplies();
		if (result.getReturnCode().equals("-1")) {
			return result;
		}
		recharge();
		return navigate(randomEnum(Destination.class));
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    };
	
	
	public void recharge() {
		if (isConnected) {
			if (powerModule.solarSailActive) {
				powerPackCapacity = getPowerPackCapacityInitial();
			} else {
				int powerModulePowerCapacity = powerModule.powerPackActive ? powerModule.powerPackCapacity - powerModule.getPowerConsumption() : 0;
				while (powerModulePowerCapacity > 0  && powerPackCapacity < getPowerPackCapacityInitial() ) {
					powerPackCapacity++;
					powerModule.powerPackCapacity--;
				}
			}
		}
	}

	/**
	 * We will be using the multiShut Util Class to make this future proof.
	 * This is because there will be more efficient shuttle types in the near future!
	 * @return final power pack capacity
	 */
	private int getPowerPackCapacityInitial() {
		MultiShuttleTypeHelperUtil multiShut = MultiShuttleTypeHelperUtil.getNewInstance();
			 InputStream powerPackCapacityIs = Optional.ofNullable(multiShut.getClazz().get()).map(multiShut::getShuttleConfigSpecs).get();
			 try {
				 powerPackCapacity = multiShut.getDischargedPowerPackCapacity(powerPackCapacityIs);
			 } catch (IOException e) {
				 powerPackCapacity = 0;
			 }
		if (powerPackCapacity == 0) {
			Logger.getLogger(multiShut.getClazz().get().getName()).log(Level.SEVERE, "Power Pack Capacity to low");
			return 0;
		}
		return (int) powerPackCapacity;
	}

	public DailyWorkResult navigate(Destination destination) {
		
		
		int powerConsumptionBase = 40;
		int powerConsumption = 0;
		switch (destination) {
		case RED_PLANET:
			powerConsumption = powerConsumptionBase << 1;
			break;
		case ORANGE_PLANET:
			powerConsumption = powerConsumptionBase << 1;
			break;
		case GREEN_PLANET:
			powerConsumption = powerConsumptionBase << 2;
			break;
		case YELLOW_PLANET:
			powerConsumption = powerConsumptionBase >> 1;
			break;
		case BLUE_PLANET:
			powerConsumption = powerConsumptionBase << 3;
			break;
		case WHITE_PLANET:
			powerConsumption = powerConsumptionBase >> 2;
			break;
		case MOON_DARK_SIDE:
			powerConsumption = powerConsumptionBase % powerConsumptionBase;
			break;
		case STATIONARY_ORBIT:
		default:
			powerConsumption = powerConsumptionBase;
		}
		DailyWorkResult result = new DailyWorkResult();
		if (powerConsumption != 0 && !(powerPackCapacity % powerConsumption <= 0)) {
			result.setReturnCode("-1");
			result.setErrorMessage("Destination cannot be reached - recharge power pack first");
		} else {
			powerPackCapacity = powerPackCapacity - powerConsumption;
			result.setReturnCode("0");
			result.setErrorMessage("Destination " + destination.toString() + " successfully reached.");
		}
		return result;
	}
	
	private DailyWorkResult getSupplies() {
		DailyWorkResult workResult = new DailyWorkResult();
		for (int i = 0; i < getNumberOfAstronauts(); i++) {
			try {
				Coffee coffee = supplyModule.makeCoffee(CoffeeType.BLACK, false);
				if (coffee == null) {
					workResult.setReturnCode("-1");
					workResult.setErrorMessage("Coffee could not be brewed. Work canceled.");
				} else {
					workResult.setReturnCode("0");
				}
			} catch (Exception e) {
				workResult.setReturnCode("-1");
				workResult.setErrorMessage("Coffee could not be brewed. Work canceled.");
			}

			supplyModule.getPizzaOven().refillSalamiStorage(100);
			
			//TODO Pizzamenge auf Basis der Astronauten einstellen, erstmal jede Sorte machen
			try {
				supplyModule.getPizzaOven().makePizza(PizzaType.CHEESY_CRUST);
				supplyModule.getPizzaOven().makePizza(PizzaType.BIG);
				supplyModule.getPizzaOven().makePizza(PizzaType.CLASSIC_25);
				supplyModule.getPizzaOven().makePizza(PizzaType.VEGAN);
			} catch (IllegalStateException e) {
				workResult.setReturnCode("-1");
				workResult.setErrorMessage("Pizza could not be baked. Work canceled.");
			}
			return workResult;
		}
		workResult.setReturnCode("1");
		return workResult;
	}

	public void setType(ShuttleType type) {
		this.type = type;
	}

	@Override
	public void tvController(boolean onOrOff) {
		// do nothing
	}

	@Override
	public void adjustRoomTemperature(int temperature) {
		// do nothing
	}
}

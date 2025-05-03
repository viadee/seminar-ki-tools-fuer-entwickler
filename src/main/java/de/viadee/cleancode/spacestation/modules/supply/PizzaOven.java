package de.viadee.cleancode.spacestation.modules.supply;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

/**
 * TODO hier sinnvoll komentieren
 */
@Component
public class PizzaOven {
	
	Integer salamiStorage = 0;

	boolean onlyVegetarianAstronauts = false;
	
	/**
	 * @param withSilces to refill the storage with if empty
	 */
	public void refillSalamiStorage(int withSilces) {
		if (salamiStorage == 0)
		{
			salamiStorage += withSilces;
		}
		else if (onlyVegetarianAstronauts)
			Logger.getLogger(PizzaOven.class.getName()).info("No salami for vegetarians!");
			salamiStorage = 0;
	}

	/**
	 * @param type
	 * @return
	 */
	public Pizza makePizza(PizzaType type) throws IllegalStateException {
		List<PizzaTopping> pizzaToppingList;
		switch (type) {
		case BIG:
			pizzaToppingList = Arrays.asList(PizzaTopping.BROCCOLI, PizzaTopping.BROCCOLI, PizzaTopping.BROCCOLI, PizzaTopping.HOLLANDAISE);
			break;
		case CHEESY_CRUST:
			pizzaToppingList = Arrays.asList(PizzaTopping.GOUDA, PizzaTopping.TOMATO, PizzaTopping.MUSHROOM, PizzaTopping.HOLLANDAISE);
			break;
		case CLASSIC_25:
			pizzaToppingList = makeSalamiPizza();
			if (pizzaToppingList == null) {
				throw new IllegalStateException("Salami storage empty");
			}
			break;
		case VEGAN:
			pizzaToppingList = Arrays.asList(PizzaTopping.GOUDA, PizzaTopping.TOMATO, PizzaTopping.MUSHROOM, PizzaTopping.BROCCOLI, PizzaTopping.HOLLANDAISE);
			break;
		default:
			pizzaToppingList = null;
			break;
		}
		// TODO Auto-generated method stub
		return new Pizza(type, pizzaToppingList);
	}

	private List<PizzaTopping> makeSalamiPizza() {
		if (salamiStorage >= 5) {
			List<PizzaTopping> pizzaToppingList;
			pizzaToppingList = Arrays.asList(PizzaTopping.SALAMI, PizzaTopping.TOMATO, PizzaTopping.HOLLANDAISE);
			salamiStorage -= 5;
			return pizzaToppingList;
		} else {
			return null;
		}
	}
}

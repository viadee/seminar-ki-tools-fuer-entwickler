package de.viadee.cleancode.spacestation.modules.habitation;

import de.viadee.cleancode.spacestation.modules.supply.Coffee;
import de.viadee.cleancode.spacestation.modules.supply.CoffeeMaker;
import de.viadee.cleancode.spacestation.modules.supply.CoffeeType;

public class EspressoMachine extends CoffeeMaker {
	
	public Coffee makeEspresso(boolean withSugar) {
		return new Coffee(CoffeeType.ESPRESSO, withSugar? 1:0 );
	}

}

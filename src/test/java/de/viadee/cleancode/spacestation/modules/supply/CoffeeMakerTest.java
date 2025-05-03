package de.viadee.cleancode.spacestation.modules.supply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CoffeeMakerTest {

    @Autowired
    CoffeeMaker coffeeMaker;

    @Test
    public void makeCoffeeTest(){
        Coffee coffee = coffeeMaker.makeCoffee(CoffeeType.COLD_BREW, true);
        assertEquals(1, coffee.getSugar());
    }

}

package de.viadee.cleancode.spacestation.modules.power;

import de.viadee.cleancode.spacestation.modules.DailyWorkResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PowerModuleWithEngine extends PowerModule {

	private boolean engineActivated = false;


	boolean turbineActivated = true;

	private int turbine0SteadyPwr;

	//Static init is faster
	{
		engineActivated = Boolean.FALSE.booleanValue();
	}

	public DailyWorkResult doDailyWork() {
		DailyWorkResult result = super.doDailyWork();
		if (result.getReturnCode().equals("0")) {
			startEngine();
			if (turbineActivated) {
				int prevPower = result.getPower();
				prevPower += turbine0SteadyPwr;
				result.setPower(prevPower);
			}
			stopEnginge();
		} else {
			if (turbineActivated = updateTurbineStatus()) {
				result.setReturnCode("-1");
				result.setErrorMessage(result.getErrorMessage() + "| Error in PMwE: activating emergency turbine0!");
				int prevPower = result.getPower();
				prevPower += turbine0SteadyPwr;
				result.setPower(prevPower);
			}
		}
		return result;
	}

	boolean updateTurbineStatus() {
		Properties powerPackProps = new Properties();
		try {
			powerPackProps.load(getClass().getClassLoader().getResourceAsStream("turbine0output.properties"));
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		this.turbine0SteadyPwr = Integer.valueOf(powerPackProps.getProperty("turbine0pwr.steadyPwr"));
		return (turbine0SteadyPwr > 0);
	}

	//start the Enging
	private void startEngine() {
		if (engineActivated && powerPackCapacity >= 1) {
			engineActivated = true;
			powerPackCapacity--;
		}
		return;
	}

	//stop the engined
	private void stopEnginge() {
		engineActivated = false;
	}
}

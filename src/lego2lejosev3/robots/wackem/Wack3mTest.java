/**
 * 
 */
package lego2lejosev3.robots.wackem;

import java.util.logging.Logger;

import lego2lejosev3.logging.Setup;
import lego2lejosev3.pblocks.Display;
import lego2lejosev3.pblocks.LargeMotor;
import lego2lejosev3.pblocks.Random;
import lego2lejosev3.pblocks.Wait;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

/**
 * Wack3m Test.
 * 
 * @author Roland Blochberger
 */
public class Wack3mTest {

	private static Class<?> clazz = Wack3mTest.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	static final Port motorPort = MotorPort.B;

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file
		Setup.log2File(clazz);
		log.fine("Starting ...");

		// instantiate the Large Motor on B
		LargeMotor motorB = new LargeMotor(motorPort);
		log.fine("Created Large Motor at Port " + motorB.getPortName());

		// Display EV3 icon on the LCD
		Display.image("EV3 icon.lni", true, 0, 0);

		// Large Motor B on for 1 second with power -30 then brake
		motorB.motorOnForSeconds(-30, 1F, true);

		// Motor Rotation B Reset
		motorB.rotationReset();

		// main loop
		while (Button.ESCAPE.isUp()) {
			// Wait for a random number of seconds between 0.1 and 3
			Wait.time(Random.numeric(0.1F, 3.0F));

			// Large Motor B on for 60 degrees with power 100 then float
			motorB.motorOnForDegrees(100, 60, false);

			// Large Motor B on for 0.5 seconds with power -40 then brake
			motorB.motorOnForSeconds(-40, 0.5F, true);

			// wait 1 second
			Wait.time(1F);
		}

		log.fine("The End");
	}

}

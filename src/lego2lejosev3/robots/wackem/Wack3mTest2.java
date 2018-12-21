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
public class Wack3mTest2 {

	private static Class<?> clazz = Wack3mTest2.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	static final Port motorPortB = MotorPort.B;
	static final Port motorPortC = MotorPort.C;

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file
		Setup.log2File(clazz);
		log.fine("Starting ...");

		// instantiate the Large Motors on B & C
		LargeMotor motorB = new LargeMotor(motorPortB);
		log.fine("Created Large Motor at Port " + motorB.getPortName());

		LargeMotor motorC = new LargeMotor(motorPortC);
		log.fine("Created Large Motor at Port " + motorC.getPortName());

		// Display EV3 icon on the LCD
		Display.image("EV3 icon.lni", true, 0, 0);

		// Large Motor B on for 1 second with power -30 then brake
		motorB.motorOnForSeconds(-30, 1F, true);
		// Large Motor C on for 1 second with power -30 then brake
		motorC.motorOnForSeconds(-30, 1F, true);

		// Motor Rotation B Reset
		motorB.rotationReset();
		// Motor Rotation C Reset
		motorC.rotationReset();

		// main loop
		for (int i = 0; Button.ESCAPE.isUp(); i++) {
			// Wait for a random number of seconds between 0.1 and 3
			Wait.time(Random.numeric(0.1F, 3F));

			if ((i % 2) == 0) {
				log.fine("Use motor B");
				// Large Motor B on for 60 degrees with power 100 then float
				motorB.motorOnForDegrees(100, 60, false);
				// Large Motor B on for 0.5 seconds with power -40 then brake
				motorB.motorOnForSeconds(-40, 0.5F, true);

			} else {
				log.fine("Use motor C");
				// Large Motor C on for 60 degrees with power 100 then float
				motorC.motorOnForDegrees(100, 60, false);
				// Large Motor C on for 0.5 seconds with power -40 then brake
				motorC.motorOnForSeconds(-40, 0.5F, true);
			}
			// wait 1 second
			Wait.time(1F);
		}

		log.fine("The End");
	}
}

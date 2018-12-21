/**
 * 
 */
package lego2lejosev3.robots.wackem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import lego2lejosev3.logging.Setup;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

/**
 * Wack3m Test.
 * 
 * @author Roland Blochberger
 */
public class Wack3mTest2PureLejos {

	private static Class<?> clazz = Wack3mTest2PureLejos.class;
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

		// instantiate the Large Motor on B & C
		EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(motorPortB);
		log.fine("Created Large Motor at Port " + motorPortB.getName());

		EV3LargeRegulatedMotor motorC = new EV3LargeRegulatedMotor(motorPortC);
		log.fine("Created Large Motor at Port " + motorPortC.getName());

		// Display EV3 icon on the LCD
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		File imageFile = new File("/home/lejos/lib/", "EV3 icon.lni");
		if (imageFile.canRead()) {
			// read the file into an image
			InputStream fis = null;
			try {
				fis = new FileInputStream(imageFile);
				Image img = Image.createImage(fis);
				g.clear();
				g.refresh();
				g.drawImage(img, 0, 0, GraphicsLCD.LEFT | GraphicsLCD.TOP);

			} catch (ArrayIndexOutOfBoundsException ie) {
				// ignore that
				//log.log(Level.WARNING,
				//		"Error displaying image file " + imageFile.getAbsolutePath() + ": " + ie.toString());

			} catch (Exception ex) {
				log.log(Level.WARNING,
						"Cannot display image file " + imageFile.getAbsolutePath() + ": " + ex.toString(), ex);

			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		} else {
			log.log(Level.WARNING, "Cannot read image file {0}", imageFile.getAbsolutePath());
		}

		// Large Motor B on for 1 second with power -30 then brake
		motorB.setSpeed((motorB.getMaxSpeed() * 30F) / 100F);
		motorB.backward();
		Delay.msDelay(1000L);
		motorB.stop();
		// Large Motor C on for 1 second with power -30 then brake
		motorC.setSpeed((motorC.getMaxSpeed() * 30F) / 100F);
		motorC.backward();
		Delay.msDelay(1000L);
		motorC.stop();

		// Motor Rotation B Reset
		motorB.resetTachoCount();
		// Motor Rotation C Reset
		motorC.resetTachoCount();

		// main loop
		for (int i = 0; Button.ESCAPE.isUp(); i++) {
			// Wait for a random number of seconds between 0.1 and 3
			Delay.msDelay(100L + (long)(Math.random() * 3000.0));

			if ((i % 2) == 0) {
				log.fine("Use motor B");
				// Large Motor B on for 60 degrees with power 100 then float
				motorB.setSpeed(motorB.getMaxSpeed());
				motorB.rotate(60);
				motorB.flt();
				// Large Motor B on for 0.5 seconds with power -40 then brake
				motorB.setSpeed((motorB.getMaxSpeed() * 40F) / 100F);
				motorB.backward();
				Delay.msDelay(500L);
				motorB.stop();

			} else {
				log.fine("Use motor C");
				// Large Motor C on for 60 degrees with power 100 then float
				motorC.setSpeed(motorC.getMaxSpeed());
				motorC.rotate(60);
				motorC.flt();
				// Large Motor C on for 0.5 seconds with power -40 then brake
				motorC.setSpeed((motorC.getMaxSpeed() * 40F) / 100F);
				motorC.backward();
				Delay.msDelay(500L);
				motorC.stop();
			}
			// wait 1 second
			Delay.msDelay(1000L);
		}
		
		// close motors
		motorB.close();
		motorC.close();

		log.fine("The End");
	}

}

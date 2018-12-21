/**
 * 
 */
package lego2lejosev3.robots.wackem;

import java.util.logging.Logger;

import lego2lejosev3.logging.Setup;
import lego2lejosev3.pblocks.BrickStatusLight;
import lego2lejosev3.pblocks.Change;
import lego2lejosev3.pblocks.Display;
import lego2lejosev3.pblocks.InfraredSensor;
import lego2lejosev3.pblocks.LargeMotor;
import lego2lejosev3.pblocks.MediumMotor;
import lego2lejosev3.pblocks.Random;
import lego2lejosev3.pblocks.Sound;
import lego2lejosev3.pblocks.Timer;
import lego2lejosev3.pblocks.TouchSensor;
import lego2lejosev3.pblocks.Wait;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Wack3m. (complete)
 * 
 * @author Roland Blochberger
 */
public class Wack3m {

	private static Class<?> clazz = Wack3m.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	private static Port motorPortA = MotorPort.A;
	private static Port motorPortB = MotorPort.B;
	private static Port motorPortC = MotorPort.C;
	private static Port touchSensorPort = SensorPort.S1;
	private static Port infraredSensorPort = SensorPort.S4;

	// the motors
	private static MediumMotor motorA = new MediumMotor(motorPortA);
	private static LargeMotor motorB = new LargeMotor(motorPortB);
	private static LargeMotor motorC = new LargeMotor(motorPortC);
	// the sensors
	private static TouchSensor touch = new TouchSensor(touchSensorPort);
	private static InfraredSensor infrared = new InfraredSensor(infraredSensorPort);

	// the variables
	private static float time;
	private static float score;

	// number of hit loops
	private static final int count = 10;

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file
		Setup.log2File(clazz);
		log.fine("Starting ...");

		// Wack3m init block
		wack3mInit();

		// unlimited game loop
		float avg = 0;
		while (Button.ESCAPE.isUp()) {
			// start game
			startGame();

			// hit loop
			int select = 0;
			for (int hit = 0; (hit < count) && Button.ESCAPE.isUp(); hit++) {

				// Prepare the hit and select the motor
				select = prepareHit();

				// Handle the hit with the selected motor
				handleHit(select);

				// sum up the score
				time += score;
			}
			// calculate the average
			avg = time / count;

			// Display text in grid coordinates 6, 3 in black with font 2 and clear screen
			// before
			// (use y = 2 for LeJOS font)
			Display.textGrid("SCORE", true, 6, 2, Display.COLOR_BLACK, Display.FONT_LARGE);
			// Display average in grid coordinates 1, 7 in black with font 2 and don't clear
			// screen before
			// (use y = 4 for LeJOS font)
			Display.textGrid(avg, false, 1, 4, Display.COLOR_BLACK, Display.FONT_LARGE);

			// select reaction sound
			if (avg <= 1F) {
				// Play sound file 'Fantastic' with volume 100 and wait until done
				Sound.playFile("Fantastic.wav", 100, Sound.WAIT);
			} else {
				// Play sound file 'Good job' with volume 100 and wait until done
				Sound.playFile("Good job.wav", 100, Sound.WAIT);
			}
			// Play sound file 'Game over' with volume 100 and wait until done
			Sound.playFile("Game over.wav", 100, Sound.WAIT);

			// Brick status light pulsing red
			BrickStatusLight.on(BrickStatusLight.COLOR_RED, BrickStatusLight.PULSE);

			// Wait 4 seconds
			Wait.time(4F);
		}

		log.fine("The End");
	}

	/**
	 * Wack3m init block
	 */
	private static void wack3mInit() {

		log.fine("");
		// Brick status light pulsing red
		BrickStatusLight.on(BrickStatusLight.COLOR_RED, BrickStatusLight.PULSE);

		// Display text in grid coordinates 5, 2 in black with font 2 and clear screen
		// before
		Display.textGrid("WACK3M", true, 5, 2, Display.COLOR_BLACK, Display.FONT_LARGE);

		// Motor B on for 1 second with power -30 and then brake
		motorB.motorOnForSeconds(-30, 1F, true);
		// Reset motor B rotation sensor
		motorB.rotationReset();

		// Motor A on with power -5
		motorA.motorOn(-5);
		// Wait 2 seconds
		Wait.time(2F);
		// Motor A off with brake
		motorA.motorOff(true);
		// Reset motor A rotation sensor
		motorA.rotationReset();

		// Motor C on for 1 second with power -30 and then brake
		motorC.motorOnForSeconds(-30, 1F, true);
		// Reset motor C rotation sensor
		motorC.rotationReset();
	}

	/**
	 * start game
	 */
	private static void startGame() {

		log.fine("");
		// Play sound file 'Start' with volume 100 and wait until done
		Sound.playFile("Start.wav", 100, Sound.WAIT);
		// Display 'Touch sensor' image file at coordinates 0,0 and clear screen before
		Display.image("Touch sensor.lni", true, 0, 0);
		// brick status light pulsing orange
		BrickStatusLight.on(BrickStatusLight.COLOR_ORANGE, BrickStatusLight.PULSE);

		// wait until touch sensor pressed
		while (Button.ESCAPE.isUp() && !touch.compareState(TouchSensor.PRESSED)) {
			Wait.time(0.005F);
		}

		// Play sound file 'Go' with volume 100 and wait until done
		Sound.playFile("Go.wav", 100, Sound.WAIT);
		// brick status light pulsing green
		BrickStatusLight.on(BrickStatusLight.COLOR_GREEN, BrickStatusLight.PULSE);

		// init variables
		time = 0;
		score = 0;

		// Wait 1 second
		Wait.time(1F);
	}

	/**
	 * Prepare the hit.
	 * 
	 * @return the selection of the motors; (1, 2, or 3).
	 */
	private static int prepareHit() {

		log.fine("");
		// brick status light pulsing green
		BrickStatusLight.on(BrickStatusLight.COLOR_GREEN, BrickStatusLight.PULSE);
		// Display 'EV3 icon' image file at coordinates 0,0 and clear screen before
		Display.image("EV3 icon.lni", true, 0, 0);

		// Wait for a random number of seconds between 0.1 and 3
		float randnr = Random.numeric(0.1F, 3.0F);
		log.fine("Wait for " + randnr + " sec.");
		Wait.time(randnr);

		// generate a random number between 1 and 90
		randnr = Random.numeric(1F, 90F);
		log.fine("Motor Select " + randnr);
		// divide by 30
		randnr /= 30F;
		log.fine("Motor Select " + randnr);
		// round up
		int select = (int) Math.ceil(randnr);
		log.fine("Select " + select);

		// just in case: limit select to 1..3
		if ((select <= 0) || (select > 3)) {
			log.warning("invalid selector " + select + " using 2 instead!");
			select = 2;
		}
		return select;
	}

	/**
	 * Handle the hit with the selected motor.
	 * 
	 * @param select the selection of the motors; (1, 2, or 3).
	 */
	private static void handleHit(int select) {

		log.fine("motor: " + select);
		// Reset Timer 1
		Timer.reset(1);

		//IR sensor change amount
		int proxDiff = 2;
		switch (select) {
		case 1:
			// Motor B on for 60 degrees with power 100 and don't brake
			motorB.motorOnForDegrees(100, 60, false);
			// Display 'Middle left' image file at coordinates 0,0 and clear screen before
			Display.image("Middle left.lni", true, 0, 0);
			// Motor B on for 0.5 seconds with power -40 and brake
			motorB.motorOnForSeconds(-40, 0.5F, true);
			// motor near IR sensor change amount 3
			proxDiff = 3;
			break;

		case 2:
			// Motor A on for 170 degrees with power 60 and don't brake
			// (runs slow, using power 100 instead)
			motorA.motorOnForDegrees(100, 170, false);
			// Display 'Neutral' image file at coordinates 0,0 and clear screen before
			Display.image("Neutral.lni", true, 0, 0);
			// Motor A on for 0.4 seconds with power -40 and brake
			motorA.motorOnForSeconds(-40, 0.4F, true);
			break;

		case 3:
			// Motor C on for 60 degrees with power 100 and don't brake
			motorC.motorOnForDegrees(100, 60, false);
			// Display 'Middle right' image file at coordinates 0,0 and clear screen before
			Display.image("Middle right.lni", true, 0, 0);
			// Motor C on for 0.5 seconds with power -40 and brake
			motorC.motorOnForSeconds(-40, 0.5F, true);
			break;
		}

		// Wait for IR Sensor detect proximity direction change of more than
		// proxDiff
		infrared.waitChangeProximity(Change.CHANGE_ANY, proxDiff);

		// Timer 1 measurement
		float time1 = Timer.measure(1);
		log.fine("Timer: " + time1);

		// Display 'Dizzy' image file at coordinates 0,0 and clear screen before
		Display.image("Dizzy.lni", true, 0, 0);
		// display timer value on grid coordinates 0, 11 with black and big font (2) and
		// no clear screen before
		// (must change y to 6 for the larger LeJOS font)
		Display.textGrid(time1, false, 0, 6, Display.COLOR_BLACK, Display.FONT_LARGE);

		// Brick status light steady red
		BrickStatusLight.on(BrickStatusLight.COLOR_RED, BrickStatusLight.CONSTANT);

		// Play sound file 'Boing' with volume 100 and wait until done
		Sound.playFile("Boing.wav", 100, Sound.WAIT);

		// set score variable
		score = time1;
	}

}

package com.codegenius.projectlethe;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.Pin;

public class CarController{


	private static final String UP="w";
	private static final String DOWN="s";
	private static final String RIGHT="d";
	private static final String LEFT="a";

	private GpioController mGpioController;
	private Wheel frontLeft, frontRight, backLeft, backRight;	

	public CarController(){
		
		mGpioController = GpioFactory.getInstance();
		frontLeft = new Wheel(RaspiPin.GPIO_23,RaspiPin.GPIO_25,RaspiPin.GPIO_24);
		frontRight = new Wheel(RaspiPin.GPIO_27,RaspiPin.GPIO_28,RaspiPin.GPIO_29);
		backLeft = new Wheel(RaspiPin.GPIO_00, RaspiPin.GPIO_03,RaspiPin.GPIO_02);
		backRight = new Wheel(RaspiPin.GPIO_06,RaspiPin.GPIO_05,RaspiPin.GPIO_04); 
	}



	public void turnRight(){
		frontLeft.forward();
		backLeft.forward();
		frontRight.backward();
		backRight.backward();	
	}

	public void turnLeft(){
		frontLeft.backward();
		backLeft.backward();
		frontRight.forward();
		backRight.forward();
	
	}

	public void moveForward(){
		frontLeft.forward();
		frontRight.forward();
		backLeft.forward();
		backRight.forward();
	}
	public void moveBackward(){
		frontLeft.backward();
		frontRight.backward();
		backLeft.backward();
		backRight.backward();
	}

	public void stop(){
		frontLeft.stop();
		frontRight.stop();
		backLeft.stop();
		backRight.stop();
	}

	private class Wheel{
		
		private Pin input1;
		private	Pin input2;
		private Pin enable;
		private GpioPinDigitalOutput gpioInput1;
		private GpioPinDigitalOutput gpioInput2;
		private GpioPinDigitalOutput gpioEnable;

		public Wheel(Pin enable, Pin in1, Pin in2){
			this.enable = enable;
			this.input1 = in1;
			this.input2 = in2;
			
			//setup Pins//
			setupWheelPins();
		}

		private void setupWheelPins(){
			this.gpioEnable = mGpioController.provisionDigitalOutputPin(enable,"ENABLE PIN",PinState.LOW);
			this.gpioInput1 = mGpioController.provisionDigitalOutputPin(input1,"Input 1",PinState.LOW);
			this.gpioInput2 = mGpioController.provisionDigitalOutputPin(input2,"Input 2",PinState.LOW);
		}

		public void forward(){
			gpioEnable.high();
			gpioInput1.high();
			gpioInput2.low();
		}

		public void backward(){
			gpioEnable.high();
			gpioInput1.low();
			gpioInput2.high();
		}	
		public void stop(){
			gpioEnable.low();
			gpioInput1.low();
			gpioInput2.low();
		}
	}

}

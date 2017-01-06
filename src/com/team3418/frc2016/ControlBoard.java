package com.team3418.frc2016;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A basic framework for the control board Like the drive code, one instance of
 * the ControlBoard object is created upon startup, then other methods request
 * the singleton ControlBoard instance.
 */
public class ControlBoard {
    private static ControlBoard mInstance = new ControlBoard();

    public static ControlBoard getInstance() {
        return mInstance;
    }
    
    //create joystick object
    private final Joystick mDriverStick;
    private final Joystick mOperatorStick;
    //private final Joystick mOtherThingie;
    
    //initialize joystick objects
    ControlBoard() {
        mDriverStick = new Joystick(0);
        mOperatorStick = new Joystick(1);
        //mOtherThingie = new Joystick(2);
    }
    
    
    
    // return button / axis info
    // EXAMPLES
    /*
    
    public double getThrottle() {
        return -mDriverStick.getY();
    }
    
    public double getTurn() {
        return mOperatorStick.getX();
    }
    
    public double getExclusiveOptions() {
        if (mOtherThingie.getRawButton(11)) {
            return 1.0;
        } else if (mOtherThingie.getRawButton(12)) {
            return -1.0;
        } else {
            return 0.0;
        }
    }
    
    public boolean getRawAxisGreaterThan() {
        return mOtherThingie.getRawAxis(3) > 0.1;
    }
    
    public boolean getRawAxisLessThan() {
        return mOtherThingie.getRawAxis(2) < -0.1;
    }
    
    public boolean getButtonCombo() {
        return mOtherThingie.getRawButton(1) && mOtherThingie.getRawButton(2);
    }
    
    */
    
    
    // DRIVER CONTROLS (mDriverStick)
    public double getLeftThrottle() {
    	return -mDriverStick.getRawAxis(1);
    }
    
    public double getRightThrottle() {
    	return -mDriverStick.getRawAxis(5);
    }
    
    public boolean getExtendIntakeButton() {
    	return mDriverStick.getRawButton(5);
    }
    
    public boolean getRetractIntakeButton() {
    	return mDriverStick.getRawButton(6);
    }
    
    public boolean getReverseIntakeRollerButton() {
    	return mDriverStick.getRawAxis(2) > .1;
    }
    
    public boolean getUtilityArmForwardButton() {
    	return mDriverStick.getRawButton(4); //top number 4
    }
    
    public boolean getUtilityArmBackwardButton() {
    	return mDriverStick.getRawButton(1); // buttom number 1
    }
    
    
    // OPERATOR CONTROLS (mOperatorStick)
    public boolean getTilterUpButton() {
    	return mOperatorStick.getRawButton(4);
    }
    
    public boolean getTilterDownButton() {
    	return mOperatorStick.getRawButton(1);
    }
    
    public boolean getFireButton() {
    	return mOperatorStick.getRawAxis(3) > .1;
    }
    
    public boolean getShooterMotorButton() {
    	return mOperatorStick.getRawButton(6);
    }
    
    public boolean getShooterMotorReverseButton() {
    	return mOperatorStick.getRawButton(5);
    }
    
    
    
}

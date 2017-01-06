package com.team3418.frc2016;

import java.util.Set;

// import classes used in main robot program
import com.team3418.frc2016.Constants;
import com.team3418.frc2016.subsystems.*;
import com.team3418.frc2016.subsystems.Intake.*;
import com.team3418.frc2016.subsystems.Popper.*;
import com.team3418.frc2016.subsystems.Shooter.*;
import com.team3418.frc2016.subsystems.Tilter.TilterState;
import com.team3418.frc2016.subsystems.UtilityArms.UtilityArmsState;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

/**
 * The main robot class, which instantiates all robot parts and helper classes.
 * After initializing all robot parts, the code sets up the autonomous.
 */
public class Robot extends IterativeRobot {
    // Subsystems
	Shooter mShooter = Shooter.getInstance();
	Intake mIntake = Intake.getInstance();
	Popper mPopper = Popper.getInstance();
	Tilter mTilter = Tilter.getInstance();
	UtilityArms mUtilityArms = UtilityArms.getInstance();
	
    // Other parts of the robot
    ControlBoard mControls = ControlBoard.getInstance();
    RobotDrive mDrive = new RobotDrive(Constants.kLeftMotorPWMID, Constants.kRightMotorPWMID);
    
    AnalogGyro mGyro = new AnalogGyro(0);
	Accelerometer mAcc = new ADXL345_I2C(Port.kOnboard,Range.k8G);//range = k8G
	
	double mNow;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    
    private void stopAllSubsystems()
	{
    	mShooter.setShooterState(ShooterState.MOTORS_STOP);
    	mIntake.setIntakeRailState(IntakeRailState.RETRACTED);
    	mIntake.setIntakeRollerState(IntakeRollerState.ROLLER_STOP);
    	mPopper.setPopperState(PopperState.NO_POP);
    	mTilter.setTilterState(TilterState.NEUTRAL);
    	mUtilityArms.setUtilityArmsState(UtilityArmsState.ARMS_STOP);
	}
    
    private void updateAllSubsystems() {
    	mShooter.updateSubsystemState();
    	mIntake.updateSubsystemState();
    	mPopper.updateSubsystemState();
    	mTilter.updateSubsystemState();
    	mUtilityArms.updateSubsystemState();
    }
    
    
    @Override
    public void robotInit() {
    	//set initial wanted states for all subsystems
    	stopAllSubsystems();
    	updateAllSubsystems();
    	mGyro.calibrate();
    }
    
    @Override
    public void disabledInit() {
    	stopAllSubsystems();
    	updateAllSubsystems();
    }
    
    @Override
    public void autonomousInit() {
    	stopAllSubsystems();
    	updateAllSubsystems();
    }
    
    @Override
    public void teleopInit() {
    	//set subsystems to state wanted at beginning of teleop
    	stopAllSubsystems();
    	updateAllSubsystems();
    	}
    
    @Override
    public void disabledPeriodic() {
    	
    }
    
    @Override
    public void teleopPeriodic() {
    	//set states of subsystems depending on operator controls or the state of other subsystems
    	
    	mNow = Timer.getFPGATimestamp();
    	
    	
    	//_________
    	
    	// popper control
    	if (mControls.getFireButton() && mShooter.getShooterReadyState() == ShooterReadyState.READY) {
    		mPopper.setPopperState(PopperState.POP);
    	} else if(mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_OUT) {
    		mPopper.setPopperState(PopperState.POP);
    	} else {	
		mPopper.setPopperState(PopperState.NO_POP);
    	}
    	//
    	
    	//_________
    	
    	// shooter motor control
    	if (mTilter.getLastTilterState() == TilterState.TILTER_UP) {
    		if (mControls.getShooterMotorButton()) {
    			mShooter.setShooterState(ShooterState.SPOOL_FAST);
    		} else if (mControls.getShooterMotorReverseButton()) {
    			mShooter.setShooterState(ShooterState.INTAKE_SLOW);
    		} else {
    			mShooter.setShooterState(ShooterState.MOTORS_STOP);
    		}
    	} else if (mTilter.getLastTilterState() == TilterState.TILTER_DOWN) {
    		if(mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_OUT) {
    			mShooter.setShooterState(ShooterState.SPOOL_SLOW);
    		} else if (mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_IN) {
    			mShooter.setShooterState(ShooterState.INTAKE_SLOW);
    		} else if (mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_STOP) {
    			mShooter.setShooterState(ShooterState.MOTORS_STOP);
    		}
    	}
    	//
    	
    	// intake control
    	
    	
    	//extend / retract button logic
    	if (mControls.getExtendIntakeButton()) {
    		mIntake.setIntakeRailState(IntakeRailState.EXTENDED);
    		mIntake.setIntakeRollerState(IntakeRollerState.ROLLER_IN);
    	} else if (mControls.getRetractIntakeButton()) {
    		mIntake.setIntakeRailState(IntakeRailState.RETRACTED);
    		mIntake.setIntakeRollerState(IntakeRollerState.ROLLER_IN);
    	} else if (mIntake.getIntakeRailState() == IntakeRailState.RETRACTED && !mControls.getReverseIntakeRollerButton()) {
    		mIntake.setIntakeRollerState(IntakeRollerState.ROLLER_STOP);
    	}
    	
    	//reverse button logic
    	if (mControls.getReverseIntakeRollerButton()) {
			mIntake.setIntakeRollerState(IntakeRollerState.ROLLER_OUT);
		} else if (mIntake.getIntakeRailState() == IntakeRailState.EXTENDED) {
			mIntake.setIntakeRollerState(IntakeRollerState.ROLLER_IN);
		}
    	//
    	
    	
    	// tilter control
    	if (mControls.getTilterUpButton()) {
    		mTilter.setTilterState(TilterState.TILTER_UP);
    	} else if(mControls.getTilterDownButton()) {
    		mTilter.setTilterState(TilterState.TILTER_DOWN);
    	} else {
    		mTilter.setTilterState(TilterState.NEUTRAL);
    	}
    	//
    	
    	// utility arms contorl
    	if (mControls.getUtilityArmForwardButton()) {
    		mUtilityArms.setUtilityArmsState(UtilityArmsState.ARMS_FORWARD);
    	} else if (mControls.getUtilityArmBackwardButton()) {
    		mUtilityArms.setUtilityArmsState(UtilityArmsState.ARMS_BACKWARDS);
    	} else {
    		mUtilityArms.setUtilityArmsState(UtilityArmsState.ARMS_STOP);
    	}
    	
    	
    	// simple drive control
    	mDrive.tankDrive(mControls.getLeftThrottle(), mControls.getRightThrottle());
    	//

    	// update subsystem states
    	updateAllSubsystems();
    	System.out.println("_____________________________________________________________________________________________");
    	System.out.println("popper = " + mPopper.getPopperState());
    	System.out.println("tilter state = " + mTilter.getTilterState()+" last tilter state = "+mTilter.getLastTilterState());
    	System.out.println("shooter state = " + mShooter.getShooterState());
    	System.out.println("utilityarm state = " + mUtilityArms.getUtilityArmState());
    	System.out.println("intake Roller state = " + mIntake.getIntakeRollerState() + " intake Rail state = " + mIntake.getIntakeRailState());
		mShooter.printEncoderVelocity();
    	//
    }
    
    @Override
    public void autonomousPeriodic() {
    	
    }
    
    /*
    private void updateDriverFeedback() {
    	
    }
    */
}

package com.team3418.frc2016;

// import classes used in main robot program
import com.team3418.frc2016.Constants;
import com.team3418.frc2016.subsystems.*;
import com.team3418.frc2016.subsystems.Intake.*;
import com.team3418.frc2016.subsystems.Shooter.*;
import com.team3418.frc2016.subsystems.Tilter.TilterState;
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
    	mShooter.stopShooterMotors();
    	mIntake.retract();
    	mIntake.stopRoller();
    	mPopper.noPop();
    	mTilter.stopTilter();
    	mUtilityArms.stopArms();
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
    		mPopper.pop();
    	} else if(mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_OUT) {
    		mPopper.pop();
    	} else {	
		mPopper.noPop();
    	}
    	//
    	
    	//_________
    	
    	// shooter motor control
    	if (mTilter.getLastTilterState() == TilterState.TILTER_UP) {
    		if (mControls.getShooterMotorButton()) {
    			mShooter.spoolFast();
    		} else if (mControls.getShooterMotorReverseButton()) {
    			mShooter.intakeSlow();
    		} else {
    			mShooter.stopShooterMotors();
    		}
    	} else if (mTilter.getLastTilterState() == TilterState.TILTER_DOWN) {
    		if(mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_OUT) {
    			mShooter.spoolSlow();
    		} else if (mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_IN) {
    			mShooter.intakeSlow();
    		} else if (mIntake.getIntakeRollerState() == IntakeRollerState.ROLLER_STOP) {
    			mShooter.stopShooterMotors();
    		}
    	}
    	//
    	
    	// intake control
    	
    	//extend / retract button logic
    	if (mControls.getExtendIntakeButton()) {
    		mIntake.extend();
    		mIntake.rollerIn();
    	} else if (mControls.getRetractIntakeButton()) {
    		mIntake.retract();
    		mIntake.rollerIn();
    	} else if (mIntake.getIntakeRailState() == IntakeRailState.RETRACTED && !mControls.getReverseIntakeRollerButton()) {
    		mIntake.stopRoller();
    	}
    	
    	//reverse button logic
    	if (mControls.getReverseIntakeRollerButton()) {
			mIntake.rollerOut();
		} else if (mIntake.getIntakeRailState() == IntakeRailState.EXTENDED) {
			mIntake.rollerIn();
		}
    	//
    	
    	
    	// tilter control
    	if (mControls.getTilterUpButton()) {
    		mTilter.tilterUp();
    	} else if(mControls.getTilterDownButton()) {
    		mTilter.tilterDown();
    	} else {
    		mTilter.stopTilter();
    	}
    	//
    	
    	// utility arms contorl
    	if (mControls.getUtilityArmForwardButton()) {
    		mUtilityArms.armsForwards();
    	} else if (mControls.getUtilityArmBackwardButton()) {
    		mUtilityArms.armsBackwards();
    	} else {
    		mUtilityArms.stopArms();
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

package com.team3418.frc2016.subsystems;

import com.team3418.frc2016.Constants;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Intake extends Subsystem
{
	static Intake mInstance = new Intake();

    public static Intake getInstance() {
        return mInstance;
    }
    
	private Solenoid mIntakeRails = new Solenoid(Constants.kIntakeRailSolenoidID);
	private Talon mIntakeSpinner = new Talon(Constants.kIntakeSpinnerID);
    
	Intake() {
		//initialize hardware settings
		System.out.println("Intake Initialized");
	}
	
    public enum IntakeRailState {
    	EXTENDED,
    	RETRACTED
    }
    
    public enum IntakeRollerState {
    	ROLLER_IN,
    	ROLLER_OUT,
    	ROLLER_STOP
    }
	
	private IntakeRailState mIntakeRailState;
	private IntakeRollerState mIntakeRollerState;

	public void setIntakeRailState(IntakeRailState state) {
		mIntakeRailState = state;
	}
	
	public IntakeRailState getIntakeRailState() {
		return mIntakeRailState;
	}
	
	public void setIntakeRollerState(IntakeRollerState state) {
		mIntakeRollerState = state;
	}

	public IntakeRollerState getIntakeRollerState() {
		return mIntakeRollerState;
	}
	
	@Override
	public void updateSubsystemState()
	{
		switch(mIntakeRailState) {
		case EXTENDED:
			setRails(true);
			break;
		case RETRACTED:
			setRails(false);
			break;
		default:
			mIntakeRailState = IntakeRailState.RETRACTED;
			break;
		}
		switch(mIntakeRollerState) {
		case ROLLER_IN:
			setRollerSpeed(Constants.kRollerIntakeSpeed);
			break;
		case ROLLER_OUT:
			setRollerSpeed(Constants.kRollerReverseSpeed);
			break;
		case ROLLER_STOP:
			setRollerSpeed(0);
			break;
		default:
			mIntakeRollerState = IntakeRollerState.ROLLER_STOP;
			break;
		}
	}
	
	private void setRails(boolean rail) {
		mIntakeRails.set(rail);
	}

	private void setRollerSpeed(double speed) {
		mIntakeSpinner.set(speed);
	}
	
}
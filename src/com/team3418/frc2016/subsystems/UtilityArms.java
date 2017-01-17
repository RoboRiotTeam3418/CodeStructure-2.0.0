package com.team3418.frc2016.subsystems;

import com.team3418.frc2016.Constants;
import com.ctre.*;
import com.ctre.CANTalon.TalonControlMode;

public class UtilityArms extends Subsystem
{
	static UtilityArms mInstance = new UtilityArms();

    public static UtilityArms getInstance() {
        return mInstance;
    }
    
	private CANTalon mLeftArm = new CANTalon(Constants.kLeftUtilityArmID);
	private CANTalon mRightArm = new CANTalon(Constants.kRightUtilityArmID);
    
	UtilityArms() {
		//initialize hardware
		System.out.println("Utility Arms Initialized");
		
		mLeftArm.changeControlMode(TalonControlMode.PercentVbus);
		mRightArm.changeControlMode(TalonControlMode.PercentVbus);
		mRightArm.enableForwardSoftLimit(false);//actually back
		mRightArm.enableReverseSoftLimit(false);//actually forward
		mLeftArm.enableForwardSoftLimit(false);//actually back
		mLeftArm.enableReverseSoftLimit(false);//actually forward
	}
	
    public enum UtilityArmsState {
    	ARMS_FORWARD, ARMS_BACKWARDS, ARMS_STOP
    }
	
	private UtilityArmsState mUtilityArmsState;
	
	public UtilityArmsState getUtilityArmState() {
		return mUtilityArmsState;
	}

	@Override
	public void updateSubsystemState()
	{
		switch(mUtilityArmsState) {
		case ARMS_BACKWARDS:
			setArmSpeed(.75);
			break;
		case ARMS_FORWARD:
			setArmSpeed(-.75);
			break;
		case ARMS_STOP:
			setArmSpeed(0);
			break;
		default:
			mUtilityArmsState = UtilityArmsState.ARMS_STOP;
			break;
		}
	}
	
	public void armsBackwards(){
		mUtilityArmsState = UtilityArmsState.ARMS_BACKWARDS;
	}
	
	public void armsForwards(){
		mUtilityArmsState = UtilityArmsState.ARMS_FORWARD;
	}
	
	public void stopArms(){
		mUtilityArmsState = UtilityArmsState.ARMS_STOP;
	}
	
	
	
	private void setArmSpeed(double speed) {
		//mLeftArm.set(speed);
		mRightArm.set(-speed);
	}

	@Override
	public void outputToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
	
}

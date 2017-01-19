package com.team3418.frc2016.subsystems;

import com.team3418.frc2016.Constants;
import com.ctre.*;
import com.ctre.CANTalon.TalonControlMode;

public class Shooter extends Subsystem
{
	
	static Shooter mInstance = new Shooter();

    public static Shooter getInstance() {
        return mInstance;
    }
    
    
    private CANTalon mLeftShooterMotor = new CANTalon(Constants.kShooterMotorLeftID);
    private CANTalon mRightShooterMotor = new CANTalon(Constants.kShooterMotorRightID);
    
    		
	Shooter() {
		//initialize shooter hardware settings
		System.out.println("Shooter Initialized");
		//leftshooter
		mLeftShooterMotor.enableBrakeMode(false);
		mLeftShooterMotor.reverseSensor(false);
		mLeftShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
		mLeftShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		mLeftShooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		
		//rightshooter
		mRightShooterMotor.enableBrakeMode(false);
		mRightShooterMotor.changeControlMode(TalonControlMode.PercentVbus);
		mRightShooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		mRightShooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		
		
		
		/*
		mRightShooterMotor.setPID(p, i, d);
		mRightShooterMotor.changeControlMode(TalonControlMode.Speed);
		mRightShooterMotor.set(0);
		*/
	}

    public enum ShooterState {
    	SPOOL_FAST, SPOOL_SLOW, INTAKE_FAST, INTAKE_SLOW, MOTORS_STOP
    }
    
    public enum ShooterReadyState {
    	READY, NOT_READY
    }
	
	private ShooterState mShooterState;
	private ShooterReadyState mShooterReadyState;

	
	public ShooterState getShooterState() {
		return mShooterState;
	}
	
	public ShooterReadyState getShooterReadyState() {
		return mShooterReadyState;
	}

	@Override
	public void updateSubsystemState()
	{
		switch(mShooterState) {
		case INTAKE_FAST:
			setSpeed(1, -1);
			break;
		case INTAKE_SLOW:
			setSpeed(.5, -.5);
			break;
		case SPOOL_FAST:
			setSpeed(-1, 1);
			break;
		case SPOOL_SLOW:
			setSpeed(-.5, .5);
			break;
		case MOTORS_STOP:
			setSpeed(0, 0);
			break;
		default:
			mShooterState = ShooterState.MOTORS_STOP;
			break;
		}
		
		//update shooter ready state
		if (mRightShooterMotor.getEncVelocity() > 37000  && mLeftShooterMotor.getEncVelocity() < -37000) {
			mShooterReadyState = ShooterReadyState.READY;
		} else {
			mShooterReadyState = ShooterReadyState.NOT_READY;
		}
		
	}
	
	public void intakeFase(){
		mShooterState = ShooterState.INTAKE_FAST;
	}
	
	public void intakeSlow(){
		mShooterState = ShooterState.INTAKE_SLOW;
	}
	
	public void spoolFast(){
		mShooterState = ShooterState.SPOOL_FAST;
	}
	
	public void spoolSlow(){
		mShooterState = ShooterState.SPOOL_SLOW;
	}
	
	public void stopShooterMotors(){
		mShooterState = ShooterState.MOTORS_STOP;
	}
	
	
	
	public void printEncoderVelocity() {
		System.out.println("left enc velocity "+mLeftShooterMotor.getEncVelocity());
		System.out.println("left enc velocity "+mRightShooterMotor.getEncVelocity());
	}
	
	
	private void setSpeed(double left, double right) {
		mLeftShooterMotor.set(left);
		mRightShooterMotor.set(right);
	}

	@Override
	public void outputToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
	
}
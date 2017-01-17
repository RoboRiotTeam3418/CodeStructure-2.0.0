package com.team3418.frc2016.subsystems;

import com.team3418.frc2016.Constants;
import edu.wpi.first.wpilibj.Solenoid;

public class Tilter extends Subsystem
{
	static Tilter mInstance = new Tilter();
	
    public static Tilter getInstance() {
        return mInstance;
    }
    
	private Solenoid mTilterUpSolenoid = new Solenoid(Constants.kTilterUpSolenoidID);
	private Solenoid mTilterDownSolenoid = new Solenoid(Constants.kTilterDownSolenoidID);
    
	Tilter() {
		//initialize hardware settings
		System.out.println("Tilter Initialized");
	}
	
    public enum TilterState {
    	TILTER_UP, TILTER_DOWN, NEUTRAL
    }
	
	private TilterState mTilterState;
	private TilterState mLastTilterState;
	
	public TilterState getTilterState() {
		return mTilterState;
	}
	
	public TilterState getLastTilterState() {
		return mLastTilterState;
	}

	@Override
	public void updateSubsystemState()
	{
		switch(mTilterState) {
		case TILTER_DOWN:
			tilterDownPosition();
			break;
		case TILTER_UP:
			tilterUpPosition();
			break;
		case NEUTRAL:
			stopTilterPosition();
			break;
		default:
			mTilterState = TilterState.NEUTRAL;
			break;
		}
	}
	
	public void tilterDown(){
		if (mTilterState != TilterState.TILTER_DOWN){
			mLastTilterState = mTilterState;
		}
		mTilterState = TilterState.TILTER_DOWN;
	}
	
	public void tilterUp(){
		if (mTilterState != TilterState.TILTER_UP){
			mLastTilterState = mTilterState;
		}
		mTilterState = TilterState.TILTER_UP;
	}
	
	public void stopTilter(){
		if (mTilterState != TilterState.NEUTRAL){
			mLastTilterState = mTilterState;
		}
		mTilterState = TilterState.NEUTRAL;
	}
	
	
	
	private void tilterUpPosition() {
		mTilterDownSolenoid.set(true);
		mTilterUpSolenoid.set(false);
	}
	
	private void tilterDownPosition() {
		mTilterDownSolenoid.set(false);
		mTilterUpSolenoid.set(true);
	}
	
	private void stopTilterPosition()
	{
		mTilterDownSolenoid.set(false);
		mTilterUpSolenoid.set(false);
	}

	@Override
	public void outputToSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
	
}

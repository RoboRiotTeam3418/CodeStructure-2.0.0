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

	public void setTilterState(TilterState state)
	{
		if (state != mTilterState) {
			mLastTilterState = mTilterState;
		}
		mTilterState = state;
	}
	
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
			tilterDown();
			break;
		case TILTER_UP:
			tilterUp();
			break;
		case NEUTRAL:
			stopTilter();
			break;
		default:
			mTilterState = TilterState.NEUTRAL;
			break;
		}
	}
	
	public void tilterUp() {
		mTilterDownSolenoid.set(true);
		mTilterUpSolenoid.set(false);
	}
	
	public void tilterDown() {
		mTilterDownSolenoid.set(false);
		mTilterUpSolenoid.set(true);
	}
	
	public void stopTilter()
	{
		mTilterDownSolenoid.set(false);
		mTilterUpSolenoid.set(false);
	}
	
}

package com.team3418.frc2016.subsystems;

import com.team3418.frc2016.Constants;
import edu.wpi.first.wpilibj.Solenoid;

public class Popper extends Subsystem
{
	static Popper mInstance = new Popper();

    public static Popper getInstance() {
        return mInstance;
    }
    
	private Solenoid mPopperSolenoid = new Solenoid(Constants.kPopperSolenoidID);
    		
	Popper() {
		//initialize hardware
		System.out.println("Popper initialized");
	}

    public enum PopperState {
    	POP, NO_POP
    }
	
	private PopperState mPopperState;

	public void setPopperState(PopperState state)
	{
		mPopperState = state;
	}
	
	public PopperState getPopperState() {
		return mPopperState;
	}

	@Override
	public void updateSubsystemState()
	{
		//System.out.println("updating popper now");
		switch(mPopperState) {
		case POP:
			setPoppper(true);
			break;
		case NO_POP:
			setPoppper(false);
			break;
		default:
			mPopperState = PopperState.NO_POP;
			break;
		}
	}
	
	private void setPoppper(boolean popper) {
		mPopperSolenoid.set(popper);
	}

}
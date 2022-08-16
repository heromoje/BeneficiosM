package com.slidingmenu.lib;

/**
 * Enabled sliding management interface 
 * 
 * @author jillanas for ameu8 
 *
 */
public interface SlidingEnablingInterface { 
	
	//////////////////////////////////////////////////////
	// ENABLED SLIDING MANAGEMENT INTERFACE 
	////////////////////////////////////////////////////// 
	
	/** 
	 * Open state constants 
	 */
	public static final int NO_OPEN_LEFT = Integer.parseInt( "01", 2 ); 
	public static final int OPEN_LEFT = Integer.parseInt( "11", 2 ); 
	public static final int NO_OPEN_RIGHT = Integer.parseInt( "10", 2 ); 
	public static final int OPEN_RIGHT = Integer.parseInt( "11", 2 ); 
	
	public void setHeightEnabledZone(int height); 
	
	public void setSlidingEnabled(int slidingEnabled); 
	
	public int getSlidingEnabled(); 
	
	public void setSlidingEnabled(boolean slidingEnabled); 
	
	public boolean isSlidingEnabled(); 
	
	public void setLeftSlidingEnabled(boolean enabled); 
	
	public boolean isLeftSlidingEnabled(); 
	
	public void setRightSlidingEnabled(boolean enabled); 
	
	public boolean isRightSlidingEnabled(); 
	
}

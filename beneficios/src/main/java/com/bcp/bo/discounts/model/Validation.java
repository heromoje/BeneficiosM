/**
 * 
 */
package com.bcp.bo.discounts.model;

import java.io.Serializable;

/**
 * The Class Validation.
 *
 * @author smarrodr
 */
public class Validation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The type of Validation. */
	private String key;
	
	/** True if validation is OK false otherwise. */
	private boolean value;
	
	/**
	 * Instantiates a new key board.
	 */
	public Validation() {
		super();
	}
	
	

	/**
	 * Constructor using paramenters
	 * @param key the validation name
	 * @param value the validatio status
	 */
	public Validation(String key, boolean value) {
		super();
		this.key = key;
		this.value = value;
	}



	/**
	 * Gets the validation name.
	 *
	 * @return the validation name
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the validation name.
	 *
	 * @param key the validation name
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the validation status.
	 *
	 * @return the validation status
	 */
	public boolean isValue() {
		return value;
	}

	/**
	 * Sets the value name.
	 *
	 * @param value the validation Status
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
}

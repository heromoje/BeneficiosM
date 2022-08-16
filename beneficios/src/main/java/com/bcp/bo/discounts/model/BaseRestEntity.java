/**
 * 
 */
package com.bcp.bo.discounts.model;

import java.io.Serializable;

/**
 * The Class BaseRestEntity.
 *
 * @author jcanopui
 */
@SuppressWarnings("serial")
public class BaseRestEntity implements Serializable {

	/** The application id. */
	int applicationId;
	
	/** The user id. */
	int userId;

	/**
	 * Instantiates a new rest request.
	 */
	public BaseRestEntity() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("BaseRestEntity [");
		buff.append(" applicationId: ").append(applicationId);
		buff.append(", userId: ").append(userId);
		buff.append("]");
		return buff.toString();
	}

	/**
	 * Gets the id application.
	 *
	 * @return the id application
	 */
	public int getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the id application.
	 *
	 * @param pApplicationId the new id application
	 */
	public void setApplicationId(final int pApplicationId) {
		this.applicationId = pApplicationId;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param pUserId the new user id
	 */
	public void setUserId(final int pUserId) {
		this.userId = pUserId;
	}
	
}

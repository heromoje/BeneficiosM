package com.bcp.bo.discounts.model;

import java.util.List;

/**
 * The Class SessionRest.
 *
 * @author jcanopui
 */
@SuppressWarnings("serial")
public class LoginSession extends BaseRestEntity {

	/** The session id. */
	private String sessionId;
	
	//JCP change 20130610
	/** The device id. */
	private int deviceId;	
	
	/** The os name. */
	private String osName;
	
	/** The os version. */
	private String osVersion;
	
	/** The brand. */
	private String brand;
	
	/** The model. */
	private String model;
	
	/** The model version. */
	private String modelVersion;
	
	/** The app version. */
	private String applicationVersion;
	
	/** The identifier of app installation version. */
	private String uuid;
	
	/** Flag that determines if update of app is compulsory */
	private Integer updateType;
	
	/** Phone number for calling BCP */
	private String contactPhone;
	
	/** Url that opens app in store */
	private String storeUrl;
	
	/** Verification String for checking that app code has non been modified */
	private String hsv;
	
	/** root validation List */
	private List<Validation> validations;
	
	/**
	 * Instantiates a new key board.
	 */
	public LoginSession() {
		super();
	}

	/**
	 * Gets the os name.
	 *
	 * @return the os name
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * Sets the os name.
	 *
	 * @param pOsName the new os name
	 */
	public void setOsName(final String pOsName) {
		this.osName = pOsName;
	}

	/**
	 * Gets the os version.
	 *
	 * @return the os version
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * Sets the os version.
	 *
	 * @param pOsVersion the new os version
	 */
	public void setOsVersion(final String pOsVersion) {
		this.osVersion = pOsVersion;
	}

	/**
	 * Gets the brand.
	 *
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * Sets the brand.
	 *
	 * @param pBrand the new brand
	 */
	public void setBrand(final String pBrand) {
		this.brand = pBrand;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param pModel the new model
	 */
	public void setModel(final String pModel) {
		this.model = pModel;
	}

	/**
	 * Gets the model version.
	 *
	 * @return the model version
	 */
	public String getModelVersion() {
		return modelVersion;
	}

	/**
	 * Sets the model version.
	 *
	 * @param pModelVersion the new model version
	 */
	public void setModelVersion(final String pModelVersion) {
		this.modelVersion = pModelVersion;
	}
	
	/**
	 * Gets the app version.
	 *
	 * @return the app version
	 */
	public String getApplicationVersion() {
		return applicationVersion;
	}

	/**
	 * Sets the app version.
	 *
	 * @param pAppVersion the new app version
	 */
	public void setApplicationVersion(final String pAppVersion) {
		this.applicationVersion = pAppVersion;
	}

	/**
	 * Gets the session id.
	 *
	 * @return the session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the session id.
	 *
	 * @param pSessionId the new session id
	 */
	public void setSessionId(final String pSessionId) {
		this.sessionId = pSessionId;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param pDeviceId the new device id
	 */
	public void setDeviceId(final int pDeviceId) {
		this.deviceId = pDeviceId;
	}
	
	/**
	 * gets root validation List 
	 * @return the validations
	 */
	public List<Validation> getValidations() {
		return validations;
	}

	/**
	 * sets root validation List
	 * @param validations the validations to set
	 */
	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}

	/**
	 * gets the identifier of app installation version
	 * @return the uuid The identifier of app installation version
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * sets the identifier of app installation version
	 * @param uuid The identifier of app installation version
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the updateType value
	 * @return the updateType value
	 */
	public Integer getUpdateType() {
		return updateType;
	}

	/**
	 * Sets the updateType value
	 * @param updateType the updateType to set
	 */
	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	/**
	 * Gets BCP phone
	 * @return the contactPhone of BCP
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * Sets BCP phone
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	/**
	 * Gets the url that points to the app in the Store
	 * @return the storeUrl of the app in Store
	 */
	public String getStoreUrl() {
		return storeUrl;
	}

	/**
	 * Sets the url that points to the app in the Store
	 * @param storeUrl the storeUrl to set
	 */
	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	/**
	 * Gets the verification code
	 * @return the hsv verification code
	 */
	public String getHsv() {
		return hsv;
	}

	/**
	 * Sets the verification code
	 * @param hsv the hsv verification code to set
	 */
	public void setHsv(String hsv) {
		this.hsv = hsv;
	}

}

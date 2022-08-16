package com.bcp.bo.discounts.social;


public interface OnOperationCompleted {
	public void onCompleted(String serviceName, boolean success, Object result);
}

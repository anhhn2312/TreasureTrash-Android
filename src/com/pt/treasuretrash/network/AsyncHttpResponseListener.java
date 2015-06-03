package com.pt.treasuretrash.network;

/**
 * Predefine some http listener methods
 * 
 */
public interface AsyncHttpResponseListener {
	/**
	 * Before get http response
	 */
	public void before();

	/**
	 * After get http response
	 * 
	 * @param statusCode
	 * @param response
	 */
	public void after(int statusCode, String response);
}

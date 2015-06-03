package com.pt.treasuretrash.network;

import org.json.JSONException;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
	@Override
	public void before() {
	}

	@Override
	public void after(int statusCode, String response) {
		
		if (statusCode != AsyncHttpBase.NETWORK_STATUS_OFF) {
			processHttpResponse(response, statusCode);
		} else {
			processIfResponseFail();
		}
	}

	/**
	 * Process HttpResponse
	 * 
	 * @param response
	 */
	public void processHttpResponse(String response, int statusCode) {
		try {
			String json = response;
			if (json == null) {
				return;
			}
			processIfResponseSuccess(json, statusCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Interface function
	 * 
	 * @throws JSONException
	 */
	public void processIfResponseSuccess(String response, int statusCode) {
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFail() {
	}
}

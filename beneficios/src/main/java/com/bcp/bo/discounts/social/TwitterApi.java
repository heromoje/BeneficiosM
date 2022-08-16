package com.bcp.bo.discounts.social;

import java.io.InputStream;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;

/**
 * Twitter api wrapper.
 * WARNING: OAuth login has to steps: Load access token and login.
 */
public class TwitterApi {
	
    public static final String TWITTER_LOGIN = "TwitterLogin";
    public static final String TWITTER_LOAD_TOKEN = "TwitterLoadToken";
    public static final String TWITTER_SHARE_MEDIA = "TwitterShareMedia";
    
	/**
	 * Twitter consumer key.
	 * Place here yours.
	 */
	static final String TWITTER_CONSUMER_KEY = "mEP7vGpddgcrJVtjDUhg"; // @CONFIG Debug keys for Twitter
	// "BAZ2mhgcyTc0pQoq7kHLNZb8qMm4MZ45ZW6Z1IuvCI" elgrupomobile
	
	/**
	 * Twitter consumer secret.
	 * Place here your.
	 */
	static final String TWITTER_CONSUMER_SECRET = "jZsKA6yZeHYO9jh3Hx7qY7dp05OfQYMMXeq8R3s"; // @CONFIG Debug keys for Twitter
	// "EnQppKSrwTHTR41QVuBg" elgrupomobile
	
	/**
	 * Twitter oauth verifier.
	 */
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	
	/**
	 * Twitter oauth token.
	 */
	private static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	/**
	 * Twitter callback url.
	 */
	public static final String TWITTER_CALLBACK_URL = "oauth://bcp_discounts";
	
	/**
	 * Twitter cancel url.
	 */
	public static final String LOAD_ACCES_TOKEN_DENIED = "LOAD_ACCES_TOKEN_DENIED";
	
	/**
	 * Twitter access token.
	 */
	private AccessToken twitterAccessToken = null;
	
	/**
	 * Twitter request token.
	 */
	private RequestToken twitterRequestToken = null;
	
	/**
	 * Get twitter access token.
	 * @return
	 */
	AccessToken getTwitterAccessToken() {
		return twitterAccessToken;
	}
	
	/**
	 * Set twitter access token.
	 * @param twitterAccessToken
	 */
	void setTwitterAccessToken(AccessToken twitterAccessToken) {
		this.twitterAccessToken = twitterAccessToken;
	}
	
	/**
	 * Get twitter requets token.
	 * @return
	 */
	RequestToken getTwitterRequestToken() {
		return twitterRequestToken;
	}
	
	/**
	 * Set twitter request token.
	 * @param twitterRequestToken
	 */
	void setTwitterRequestToken(RequestToken twitterRequestToken) {
		this.twitterRequestToken = twitterRequestToken;
	}

	/**
	 * Check user already logged in twitter.
	 * @return True when logged in, false when not.
	 */
	public boolean isLoggedInAlready() {
		return twitterAccessToken != null && twitterRequestToken != null;
	}
	
	/**
	 * Login to twitter.
	 * @param callback Callback interface.
	 */
	public void login(final OnOperationCompleted operationCompleted) {
		(new RequestTwitterLoginTask(operationCompleted, this)).execute();
	}
	
	/**
	 * Logout from twitter.
	 */
	public void logout() {
		twitterAccessToken = null;
		twitterRequestToken = null;
	}
	
	/**
	 * Load the twitter access token.
	 * If "denied" token is found it calls: operationCompleted.onCompleted(TWITTER_LOAD_TOKEN, false, LOAD_ACCES_TOKEN_DENIED);
	 * @param callback Callback interface.
	 * @param url Url to load the access token.
	 */
	public void loadAccessToken(final OnOperationCompleted operationCompleted, final String url, final Context context) {		
	
		if (url.contains(URL_TWITTER_OAUTH_TOKEN) && url.contains(URL_TWITTER_OAUTH_VERIFIER)) {
			(new LoadTwitterAccessTokenTask(operationCompleted, this)).execute(url);
		} else {
			operationCompleted.onCompleted(TWITTER_LOAD_TOKEN, false, LOAD_ACCES_TOKEN_DENIED);
		}
		
	}
	
	/**
	 * Share on twitter text and media.
	 * @param iDetailItem Callback interface.
	 * @param message Text to share.
	 * @param media Image to share.
	 */
	public void share(final OnOperationCompleted operationCompleted, final String message, InputStream media) {
		(new UpdateTwitterStatusTask(operationCompleted, this, message, media)).execute();
	}
	
	/**
	 * Get user screen name.
	 * @return User name.
	 */
	public String getUserScreenName() {
		String exit = "NotLogged";
		if (twitterAccessToken != null) {
			exit = twitterAccessToken.getScreenName();
		}
		return exit;
	}

}

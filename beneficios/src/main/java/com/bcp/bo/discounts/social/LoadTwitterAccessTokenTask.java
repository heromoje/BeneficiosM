package com.bcp.bo.discounts.social;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Load twitter access token and call detail twitter unload to return 
 * to detail item. 
 */
class LoadTwitterAccessTokenTask extends AsyncTask<String, Void, Void> {
	
	private TwitterApi twitterApi;
	private OnOperationCompleted operationCompleted;
	private TwitterException ex;
	
	public LoadTwitterAccessTokenTask(final OnOperationCompleted operationCompleted, TwitterApi twitterApi) {
		this.twitterApi = twitterApi;
		this.operationCompleted = operationCompleted;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		AccessToken accessToken = null;
		try {
			accessToken = run(params[0]);
			twitterApi.setTwitterAccessToken(accessToken);
		} catch (TwitterException e) {
			ex = e;
		}		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if(ex == null) {
			super.onPostExecute(result);
			operationCompleted.onCompleted(TwitterApi.TWITTER_LOAD_TOKEN, true, null);
		} else {
			operationCompleted.onCompleted(TwitterApi.TWITTER_LOAD_TOKEN, false, ex.getMessage());
		}
	}
	
	private AccessToken run(final String url) throws TwitterException {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterApi.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterApi.TWITTER_CONSUMER_SECRET);
			
	    Twitter twitter = new TwitterFactory(builder.build()).getInstance();
	    	
	    Uri uri = Uri.parse(url);
	    String verifier = uri.getQueryParameter(TwitterApi.URL_TWITTER_OAUTH_VERIFIER);
	    
	    // Get the access token
	    return twitter.getOAuthAccessToken(twitterApi.getTwitterRequestToken(), verifier);  
	}

}

package com.bcp.bo.discounts.social;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;

/**
 * Request for twitter login.
 */
class RequestTwitterLoginTask extends AsyncTask<Void, Void, Void> {

	private OnOperationCompleted operationCompleted;
	private TwitterApi twitterApi;
	private TwitterException ex;
	
	public RequestTwitterLoginTask(final OnOperationCompleted operationCompleted, TwitterApi twitterApi) {
		this.operationCompleted = operationCompleted;
		this.twitterApi = twitterApi;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		RequestToken requestToken = null;
		try {
			requestToken = run();
		} catch (TwitterException e) {
			ex = e;
		}
		twitterApi.setTwitterRequestToken(requestToken);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if(ex == null) {
			super.onPostExecute(result);
			operationCompleted.onCompleted(
					TwitterApi.TWITTER_LOGIN, 
					true, 
					twitterApi.getTwitterRequestToken().getAuthenticationURL());
		} else {
			operationCompleted.onCompleted(
					TwitterApi.TWITTER_LOGIN, 
					false, 
					ex.getMessage());
		}
	}

	private RequestToken run() throws TwitterException {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterApi.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterApi.TWITTER_CONSUMER_SECRET);
	
		Twitter twitter = new TwitterFactory(builder.build()).getInstance();
		return twitter.getOAuthRequestToken(TwitterApi.TWITTER_CALLBACK_URL);	
	}

}

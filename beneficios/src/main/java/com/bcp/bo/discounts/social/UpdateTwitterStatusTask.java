package com.bcp.bo.discounts.social;

import java.io.InputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;

/**
 * Share a message and/or media on twitter showing a loading dialog 
 * between start and end.
 */
class UpdateTwitterStatusTask extends AsyncTask<Void, Void, Void> {

	private TwitterApi twitterApi;
	private String message;
	private InputStream media;
	private OnOperationCompleted operationCompleted;
	private TwitterException ex;
	
	public UpdateTwitterStatusTask(final OnOperationCompleted operationCompleted, TwitterApi twitterApi, 
			final String message, final InputStream media) {
		this.operationCompleted = operationCompleted;
		init(twitterApi, message, media);
	}
	
	private void init(TwitterApi twitterApi, final String message, final InputStream media) {
		this.twitterApi = twitterApi;
		this.message = message;
		this.media = media;
	}

	@Override
	protected void onPostExecute(Void result) {	
		if(ex == null) {
			super.onPostExecute(result);
			operationCompleted.onCompleted(TwitterApi.TWITTER_SHARE_MEDIA, true, null);
		} else {
			operationCompleted.onCompleted(TwitterApi.TWITTER_SHARE_MEDIA, false, ex.getMessage());
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			run();
		} catch (TwitterException e) {
			ex = e;
		}
		return null;
	}

	private void run() throws TwitterException {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterApi.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterApi.TWITTER_CONSUMER_SECRET);
		
		Twitter twitter = new TwitterFactory(builder.build()).getInstance(twitterApi.getTwitterAccessToken());
		
		// Update status with media
		if(message == null) {
			message = "";
		}
		StatusUpdate statusUpdate = new StatusUpdate(message);
		if(media != null) {
			statusUpdate.setMedia(String.valueOf(System.currentTimeMillis()), media);
		}
		twitter.updateStatus(statusUpdate);
	}

}

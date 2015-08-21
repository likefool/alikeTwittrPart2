package com.likefool.apps.aliketweetr.helpers;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "ya4dO6CFxIpfgecF3cPcJhaef";       // Change this
	public static final String REST_CONSUMER_SECRET = "xxK4PWxSJnbERyIADldCYJiMFTzHyXwDLK8TDAZEQZcWJvRQ8O"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets"; // Change this (here and in manifest)

	private TwitterClientFilter filter;

	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
		filter = new TwitterClientFilter();
	}

	public TwitterClientFilter getFilter() {
		return filter;
	}

	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", filter.getHomeCount());
		params.put("exclude_replies", false);
		Long maxId = filter.getHomeMaxIdId();
		if (maxId > 0) {
			params.put("max_id", maxId);
		}
		//params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	// Get Mention Timeline /statuses/mentions_timeline.json
	// count = 25
	// since_id=1
	public void getMentionTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", filter.getMentionCount());
		params.put("exclude_replies", false);
		Long maxId = filter.getMentionMaxId();
		if (maxId > 0) {
			params.put("max_id", maxId);
		}
		//params.put("page", String.valueOf(page));
		// Execute Request
		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(int page, AsyncHttpResponseHandler handler, String screenName) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", filter.getUserCount());
		Long maxId = filter.getUserMaxId();
		if (maxId > 0) {
			params.put("max_id", maxId);
		}
		//params.put("page", String.valueOf(page));
		params.put("screen_name", screenName);
		// Execute Request
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

    public void getUserInfo(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        //RequestParams params = new RequestParams();
        getClient().get(apiUrl, null, handler);
    }

	public void getUserProfile(JsonHttpResponseHandler handler) {
		Log.d("my", "call getting user profile api");
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, new RequestParams(), handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
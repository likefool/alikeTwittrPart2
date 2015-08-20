package com.likefool.apps.aliketweetr.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by aliku on 2015/8/20.
 */
public class TwitterUser implements Serializable {
    // list attribute
    private String name;
    private String location;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String profileBgImageUrl;
    private int favoriteCount;
    private int followersCount;
    private int followingCount;
    private int tweetsCount;
    // deserialize the user json


    public String getName() {
        return name;
    }
    public long getUid() {
        return uid;
    }
    public String getScreenName() {
        return screenName;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public String getLocation() {return location; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setUid(long uid) { this.uid = uid; }
    public void setScreenName(String screenName) { this.screenName = screenName; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public TwitterUser(JSONObject json) {
        try {
            name = json.getString("name");
            uid = json.getLong("id");
            screenName = json.getString("screen_name");
            profileImageUrl = json.getString("profile_image_url");
            profileBgImageUrl = json.getString("profile_background_image_url");
            location = json.getString("location");
            favoriteCount = json.getInt("favourites_count");
            followersCount = json.getInt("followers_count");
            followingCount = json.getInt("friends_count");
            tweetsCount = json.getInt("statuses_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public TwitterUser() {}

    public String getProfileBgImageUrl() {
        return profileBgImageUrl;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }
}
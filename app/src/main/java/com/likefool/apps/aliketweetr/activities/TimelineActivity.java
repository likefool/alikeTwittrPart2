package com.likefool.apps.aliketweetr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.likefool.apps.aliketweetr.R;
import com.likefool.apps.aliketweetr.RestApplication;
import com.likefool.apps.aliketweetr.adapters.TimelineFragmentPagerAdapter;
import com.likefool.apps.aliketweetr.fragments.TimelineFragment;
import com.likefool.apps.aliketweetr.models.TwitterUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity implements TimelineFragment.OnFragmentInteractionListener {

    private TwitterUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager vPager = (ViewPager) findViewById(R.id.viewpager);
        vPager.setAdapter(new TimelineFragmentPagerAdapter(getSupportFragmentManager()));
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(vPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.twitter_post) {
            goPost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goPost() {
        Intent i = new Intent(this, PostActivity.class);
        startActivityForResult(i, 3458);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        if(resultCode == RESULT_OK){

            tweetAdapter.clear();
            populateTimeline(1);
        }
        */
    }

    @Override
    public void onFragmentInteraction() {
        return;

    }

    public void onClickProfile(MenuItem item) {
        RestApplication.getRestClient().getUserProfile(new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = new TwitterUser(response);
                goUserProfile(user);
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("my", "verify credentials api fail" + errorResponse.toString());
            }
        });

    }

    public void goUserProfile(TwitterUser user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}

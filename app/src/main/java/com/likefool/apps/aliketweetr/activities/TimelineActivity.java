package com.likefool.apps.aliketweetr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.likefool.apps.aliketweetr.R;
import com.likefool.apps.aliketweetr.adapters.TimelineFragmentPagerAdapter;
import com.likefool.apps.aliketweetr.adapters.TweetsArrayAdapter;
import com.likefool.apps.aliketweetr.fragments.TimelineFragment;
import com.likefool.apps.aliketweetr.helpers.RestClient;
import com.likefool.apps.aliketweetr.models.Tweet;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements TimelineFragment.OnFragmentInteractionListener {

    private RestClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetAdapter;
    private ListView lvTweets;


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
}

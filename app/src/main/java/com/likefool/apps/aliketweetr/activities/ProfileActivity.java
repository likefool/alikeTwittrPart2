package com.likefool.apps.aliketweetr.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.likefool.apps.aliketweetr.R;
import com.likefool.apps.aliketweetr.fragments.TimelineFragment;
import com.likefool.apps.aliketweetr.helpers.RestClient;
import com.likefool.apps.aliketweetr.models.TwitterUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends ActionBarActivity implements TimelineFragment.OnFragmentInteractionListener{
    private TwitterUser user;
    private RestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // parse user
        user = (TwitterUser) getIntent().getSerializableExtra("user");

        // get user timeline
        if (getSupportActionBar().isShowing() == true) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flUserTimeline, TimelineFragment.newInstance(user));
            fragmentTransaction.commit();
        }

        // set data
        fillProfileData();
    }

    private void fillProfileData() {
        // user icon
        ImageView ivUserIcon = (ImageView) findViewById(R.id.ivUserIcon);
        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .fit()
                .into(ivUserIcon);
        // user name
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(user.getName());
        // user screen name
        TextView tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
        tvUserScreenName.setText("@" + user.getScreenName());
        // profile bg img
        ImageView ivProfileBgImg = (ImageView) findViewById(R.id.ivProfileBgImg);
        Picasso.with(this)
                .load(user.getProfileBgImageUrl())
                .fit()
                .into(ivProfileBgImg);
        // tweet count
        TextView tvTweetsCount = (TextView) findViewById(R.id.tvTweetsCount);
        tvTweetsCount.setText(String.valueOf(user.getTweetsCount()));
        // following count
        TextView tvFollowingCount = (TextView) findViewById(R.id.tvFollowingsCount);
        tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));
        // follower count
        TextView tvFollowersCount = (TextView) findViewById(R.id.tvFollowersCount);
        tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction() {

        return;
    }
}

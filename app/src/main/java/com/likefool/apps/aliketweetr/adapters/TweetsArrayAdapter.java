package com.likefool.apps.aliketweetr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.likefool.apps.aliketweetr.R;
import com.likefool.apps.aliketweetr.activities.TimelineActivity;
import com.likefool.apps.aliketweetr.helpers.ParseRelativeData;
import com.likefool.apps.aliketweetr.models.Tweet;
import com.likefool.apps.aliketweetr.models.TwitterUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aliku on 2015/8/13.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    Context context;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        this.context = context;
    }

    // override and setup custom template


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUser(tweet.getUser());
            }
        });

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUser(tweet.getUser());
            }
        });

        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTime.setText(new ParseRelativeData().getRelativeTimeAgo(tweet.getCreatedAt()));
        ivProfileImage.setImageResource(android.R.color.transparent); // clear out the old image for recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        return convertView;
    }

    public void onClickUser(TwitterUser twitterUser) {
        ((TimelineActivity) context).goUserProfile(twitterUser);
    }
}

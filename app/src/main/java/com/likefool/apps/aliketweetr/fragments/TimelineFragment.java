package com.likefool.apps.aliketweetr.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.likefool.apps.aliketweetr.R;
import com.likefool.apps.aliketweetr.RestApplication;
import com.likefool.apps.aliketweetr.adapters.TweetsArrayAdapter;
import com.likefool.apps.aliketweetr.helpers.EndlessScrollListener;
import com.likefool.apps.aliketweetr.helpers.RestClient;
import com.likefool.apps.aliketweetr.models.Tweet;
import com.likefool.apps.aliketweetr.models.TwitterUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TAB = "tab";

    // TODO: Rename and change types of parameters
    private String mTab;

    private OnFragmentInteractionListener mListener;

    private LayoutInflater inflater;
    private View rootView;

    private RestClient client;
    private TwitterUser user;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetAdapter;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tab Parameter 1.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String tab) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }

    public static TimelineFragment newInstance(TwitterUser user) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAB, "profile");
        fragment.setArguments(args);
        fragment.user = user;
        return fragment;
    }

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTab = getArguments().getString(ARG_TAB);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvTweets = (ListView) rootView.findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetsArrayAdapter(getActivity(), tweets);
        lvTweets.setAdapter(tweetAdapter);
        client = RestApplication.getRestClient(); // singleton client
        tweetAdapter.clear();
        populateTimeline(1);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline(page);
            }
        });

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                tweetAdapter.clear();
                populateTimeline(1);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void populateTimeline(int i) {
        if (mTab == "profile") {
            if (i == 1) {
                client.getFilter().resetUserTimeline();
                tweetAdapter.clear();

            }
            client.getUserTimeline(i, new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                    tweetAdapter.addAll(newTweets);
                    client.getFilter().updateUserMaxId(newTweets);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    if (response != null) {
                        Log.e("ERROR", "populateTimeline fail, response: " + response.toString());
                    }
                    //Toast.makeText(this, "Timeline Fail", Toast.LENGTH_SHORT).show();

                    // Query ActiveAndroid for list of data
                    List<Tweet> localTweets = new Select().from(Tweet.class)
                            .orderBy("CreatedAt DESC").limit(100).execute();
                    tweetAdapter.addAll(localTweets);
                    swipeContainer.setRefreshing(false);

                }


            }, user.getScreenName());
        } else if (mTab == "mention") {
            if (i == 1) {
                client.getFilter().resetMentionTimeline();
                tweetAdapter.clear();
            }
            client.getMentionTimeline(i, new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                    tweetAdapter.addAll(newTweets);
                    client.getFilter().updateMentionMaxId(newTweets);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    if (response != null) {
                        Log.e("ERROR", "populateTimeline fail, response: " + response.toString());
                    }
                    //Toast.makeText(this, "Timeline Fail", Toast.LENGTH_SHORT).show();

                    // Query ActiveAndroid for list of data
                    List<Tweet> localTweets = new Select().from(Tweet.class)
                            .orderBy("CreatedAt DESC").limit(100).execute();
                    tweetAdapter.addAll(localTweets);
                    swipeContainer.setRefreshing(false);

                }


            });
        } else {
            if (i == 1) {
                client.getFilter().resetHomeTimeline();
                tweetAdapter.clear();

            }
            client.getHomeTimeline(i, new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                    tweetAdapter.addAll(newTweets);
                    client.getFilter().updateHomeMaxId(newTweets);

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    if (response != null) {
                        Log.e("ERROR", "populateTimeline fail, response: " + response.toString());
                    }
                    //Toast.makeText(this, "Timeline Fail", Toast.LENGTH_SHORT).show();

                    // Query ActiveAndroid for list of data
                    List<Tweet> localTweets = new Select().from(Tweet.class)
                            .orderBy("CreatedAt DESC").limit(100).execute();
                    tweetAdapter.addAll(localTweets);
                    swipeContainer.setRefreshing(false);

                }


            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

}

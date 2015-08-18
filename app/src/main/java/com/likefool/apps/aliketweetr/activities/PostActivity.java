package com.likefool.apps.aliketweetr.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.likefool.apps.aliketweetr.R;
import com.likefool.apps.aliketweetr.RestApplication;
import com.likefool.apps.aliketweetr.helpers.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class PostActivity extends ActionBarActivity {
    private ImageView ivUserImage;
    private TextView tvUserName;
    private EditText etPost;
    private RestClient client;
    private TextView tvPostCount;

    final TextWatcher txwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvPostCount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvPostCount = (TextView) findViewById(R.id.tvCount);
        etPost = (EditText) findViewById(R.id.etPost);
        etPost.addTextChangedListener(txwatcher);

        client = RestApplication.getRestClient();
        showUserInfo();
    }

    private void showUserInfo() {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String userName = response.getString("name");
                    String userImageUrl = response.getString("profile_image_url_https");
                    tvUserName.setText(userName);
                    Picasso.with(getApplicationContext()).load(userImageUrl).into(ivUserImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
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

    public void doCancel(View view) {
        finish();
    }

    public void doPost(View view) {
        String content = etPost.getText().toString();
        client.postTweet(content, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getApplicationContext(), "Post Success", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Post Fail", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }
}

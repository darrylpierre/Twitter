package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimeLineActivity extends AppCompatActivity {

    private RecyclerView rvTweets;
    private TwitterClient client;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = TwitterApp.getRestClient(this);

        // Find the recycler view
          rvTweets = findViewById(R.id.rvTweets);
        // Initialize list of tweets and adapter from the data source
        tweets = new ArrayList<>();
        adapter =new TweetsAdapter(this, tweets);
        // Recycler view setup: layout manager and setting the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               // Toast.makeText(TimeLineActivity.this, "Success", Toast.LENGTH_SHORT).show();
              //  Log.d("darryl", response.toString());
                // Iterate through the list of tweets
                for (int i = 0; i<response.length(); i++ ) {
                    try {
                        // Convert each Json object into a tweet object
                        JSONObject jsonTweetObject = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJson(jsonTweetObject);

                        // Add the tweet into our data source
                        tweets.add(tweet);
                        Log.d("darryl", tweet.toString());
                        // Notify adapter
                        adapter.notifyItemInserted(tweets.size() - 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(TimeLineActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(TimeLineActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

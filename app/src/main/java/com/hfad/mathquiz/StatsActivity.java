package com.hfad.mathquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.hfad.mathquiz.utilities.Background;
import com.hfad.mathquiz.utilities.DBManager;
import com.hfad.mathquiz.utilities.TwitterUser;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class StatsActivity extends AppCompatActivity {

    private static final int AUTHENTICATE = 1;
    private Twitter twitter = TwitterFactory.getSingleton();
    private TwitterUser twitterUser = TwitterUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DBManager dbManager = DBManager.getInstance(this);
        TextView scoresView = (TextView) findViewById(R.id.scoresView);
        scoresView.setText(dbManager.databaseToString());
    }

    public void onTwitLogin(View view) {
        Intent intent = new Intent(this, Authenticate.class);
        startActivityForResult(intent, AUTHENTICATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == AUTHENTICATE && resultCode == RESULT_OK) {
            Background.run(new Runnable() {
                @Override
                public void run() {
                    String token = data.getStringExtra("access token");
                    String secret = data.getStringExtra("access token secret");
                    AccessToken accessToken = new AccessToken(token, secret);
                    twitter.setOAuthAccessToken(accessToken);
                    twitterUser.setLoggedIn(true);
                }
            });
        }
    }

    public void onTweetButton(View view) {
        final DBManager dbManager = DBManager.getInstance(this);
        Background.run(new Runnable() {
            @Override
            public void run() {
                if(twitterUser.isLoggedIn()) {
                    if(!dbManager.databaseToString().equals("")) {
                        try {
                            twitter.updateStatus("My highest score in MathQuiz is " +
                                    dbManager.getMaxScore() +
                                    "! Download the game and try to beat me!");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(StatsActivity.this, "Tweet sent!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StatsActivity.this, "You need to login first.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

package com.hfad.mathquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.hfad.mathquiz.models.Answer;
import com.hfad.mathquiz.models.Ball;
import com.hfad.mathquiz.models.CollisionListener;
import com.hfad.mathquiz.models.NewScore;
import com.hfad.mathquiz.models.QuestionManager;
import com.hfad.mathquiz.utilities.Background;
import com.hfad.mathquiz.utilities.DBManager;
import com.hfad.mathquiz.utilities.SoundManager;
import com.hfad.mathquiz.utilities.TwitterUser;
import com.hfad.mathquiz.views.AnswerView;
import com.hfad.mathquiz.views.BallView;
import com.hfad.mathquiz.views.NewScoreView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class GameActivity extends AppCompatActivity implements SensorEventListener,
        GestureDetector.OnGestureListener, CollisionListener {

    private Toolbar toolbar;
    private QuestionManager questionManager;
    private TextView progressView;
    private TextView problemView;
    private BallView ballView;
    private Ball ball;
    private NewScoreView newScoreView;
    private NewScore newScore;
    private AnswerView answerLeftView;
    private AnswerView answerRightView;
    private Answer leftAnswer;
    private Answer rightAnswer;
    private SharedPreferences settingsPref;
    private SoundManager soundManager;
    private int wowSound, chimeSound, wrongSound;
    private GestureDetectorCompat gestureDetector;
    private Thread drawingThread = new Thread(new Runnable() {

        private boolean running;

        @Override
        public void run() {
            try {
                running = true;
                while (running) {
                    Thread.sleep(50);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ballView.invalidate();
                            answerLeftView.invalidate();
                            answerRightView.invalidate();
                            newScoreView.invalidate();
                        }
                    });
                    setAnsViewDimens();
                    if(ball.isShooting()) {
                        ball.decreaseY();
                        ball.detectCollision();
                    }
                    leftAnswer.animate();
                    rightAnswer.animate();
                    newScore.animate();
                }
            } catch (InterruptedException ignored) {
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressView = (TextView) findViewById(R.id.progressView);
        problemView = (TextView) findViewById(R.id.problemView);
        answerLeftView = (AnswerView) findViewById(R.id.answerLeftView);
        answerRightView = (AnswerView) findViewById(R.id.answerRightView);
        soundManager = new SoundManager(this);
        wowSound = soundManager.addSound(R.raw.wow);
        chimeSound = soundManager.addSound(R.raw.chime);
        wrongSound = soundManager.addSound(R.raw.wrong);
        ballView = (BallView) findViewById(R.id.ballView);
        ball = new Ball(this);
        ballView.setModel(ball);
        newScoreView = (NewScoreView) findViewById(R.id.newScoreView);
        newScore = new NewScore();
        newScoreView.setModel(newScore);
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        leftAnswer = new Answer(true, screenWidth);
        rightAnswer = new Answer(false, screenWidth);
        answerLeftView.setModel(leftAnswer);
        answerRightView.setModel(rightAnswer);
        questionManager = new QuestionManager();
        settingsPref = PreferenceManager.getDefaultSharedPreferences(this);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mySensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.gestureDetector = new GestureDetectorCompat(this, this);
        startGame();
    }

    private void startGame() {
        questionManager.initialize(Integer.parseInt(settingsPref.getString("num_of_questions",
                "5")), settingsPref.getString("difficulty_selection", "Easy"));
        questionManager.nextQuestion();
        updateViews();
        drawingThread.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        int toolbarHeight = toolbar.getHeight();
        ball.setBallViewHeight(ballView.getHeight());
        ball.setLeftAnsViewHeight(answerLeftView.getY() + toolbarHeight);
        ball.setRightAnsViewHeight(answerRightView.getY() + toolbarHeight);
        ball.reset();
    }

    private void setAnsViewDimens() {
        float[] leftAnsViewDimens = {answerLeftView.getX(), (answerLeftView.getX() +
                answerLeftView.getWidth())};
        ball.setLeftAnsViewDimens(leftAnsViewDimens);
        float[] rightAnsViewDimens = {answerRightView.getX(), (answerRightView.getX() +
                answerRightView.getWidth())};
        ball.setRightAnsViewDimens(rightAnsViewDimens);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_stats) {
            Intent intent = new Intent(this, StatsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkAnswer(final String providedAnswer) {
        GameActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(questionManager.checkAnswer(providedAnswer)) {
                    Toast.makeText(GameActivity.this, "correct!", Toast.LENGTH_SHORT)
                            .show();
                    soundManager.play(chimeSound);
                }
                else {
                    Toast.makeText(GameActivity.this, "incorrect!", Toast.LENGTH_SHORT)
                            .show();
                    soundManager.play(wrongSound);
                }
                questionManager.nextQuestion();
                updateViews();
            }
        });
    }

    private void updateViews() {
        if(!questionManager.isGameOver()) {
            progressView.setText(questionManager.getProgressText());
            problemView.setText(questionManager.getProblemText());
            answerLeftView.setText(questionManager.getAnswerLeftText());
            answerRightView.setText(questionManager.getAnswerRightText());
        } else {
            DBManager dbManager = DBManager.getInstance(this);
            int score = questionManager.getCorrectAnswersTally();
            if(dbManager.addHighscore(score)) {
                newScoreView.setVisibility(View.VISIBLE);
                soundManager.play(wowSound);
                updateTwitter(score);
            }
            progressView.setText(getString(R.string.prog_view_game_over,
                    questionManager.getCorrectAnswersTally()));
            problemView.setVisibility(View.INVISIBLE);
            answerLeftView.setVisibility(View.INVISIBLE);
            answerRightView.setVisibility(View.INVISIBLE);
            ballView.setVisibility(View.GONE);
        }
    }

    private void updateTwitter(final int score) {
        Background.run(new Runnable() {
            @Override
            public void run() {
                TwitterUser twitterUser = TwitterUser.getInstance();
                if(settingsPref.getBoolean("twitter_updates_switch", false)
                        && twitterUser.isLoggedIn()) {
                    Twitter twitter = TwitterFactory.getSingleton();
                    try {
                        twitter.updateStatus("I just got a high score of " + score
                                + " in MathQuiz! Download the game and try to beat me!");
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int xValue = (int) event.values[0] * -1;
        int normalizedxVal = (int) (100.0*((xValue - (-9.0))/(9.0 - (-9.0))));
        int scaledX = (normalizedxVal * (0 - ballView.getWidth())) * -1 / 100;
        ball.moveX(scaledX);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onCollision(String collidedWith) {
        if(collidedWith.equals("left"))
            checkAnswer(answerLeftView.getText().toString());
        else
            checkAnswer(answerRightView.getText().toString());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(!(velocityX > 700 || velocityX < -700 || velocityY > 0))
            ball.shoot(velocityY);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }
}

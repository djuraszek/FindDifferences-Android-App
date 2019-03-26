package com.cookbook.android.znajdzroznice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {


    TextView score, maxScore;
    Button nextButton, exitButton;

    String currentUsername, currentTime;
    String[] currentSongArray;
    ListView songLV;
    ListviewAdapter lvAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.println(Log.ERROR, "Score Activity", "onCreate");
        setContentView(R.layout.activity_score);

        getSettingsChoices();
        hideNavigationBar();

        setValues(this);
        Log.d("ScoreActivity", "onCreate");
    }

    public void setValues(final Context context){
        Log.println(Log.ERROR, "Score Activity", "setValues");
        songLV = (ListView)findViewById(R.id.scoreListView);
        lvAdapter = new ListviewAdapter(context, );
        songLV.setAdapter(lvAdapter);


//        nextButton = (Button) findViewById(R.id.nextButton);
//        exitButton = (Button) findViewById(R.id.exitButton);
            nextButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.VISIBLE);
//        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.println(Log.ERROR, "Score Activity", "-->nextButton");

                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtra("currentPlayer", currentPlayer);
                intent.putExtra("playerNr",PLAYER_NUMBER);
                intent.putExtra("songNr",SONG_NUMBER);
                intent.putExtra("genre",GENRE);
                intent.putExtra("decade",DECADE);

                Log.println(Log.INFO, "Score Activity to Question Activity",
                        "values:\nsong nr "+ SONG_NUMBER+"\nplayer_number "+PLAYER_NUMBER
                                +"\ndecade "+ DECADE + "\ngenre "+GENRE);

                startActivity(intent);
                finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.println(Log.ERROR, "Score Activity", "-->exitButton");
                //get back to settings activity and kill the rest of activities
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getSettingsChoices(){
        Log.println(Log.ERROR, "Score Activity", "settingsChoices");

        currentUsername = getIntent().getStringExtra("username");
        currentTime = getIntent().getStringExtra("time");

//        Log.println(Log.INFO, "Score Activity", "values:\nsong nr "+SONG_NUMBER+"\nplayer_number "+PLAYER_NUMBER
//                +"\ndecade "+ DECADE + "\ngenre "+GENRE);


    }

    @Override
    protected void onResume(){
        Log.println(Log.ERROR, "Score Activity", "*****onResume******");
        super.onResume();
        hideNavigationBar();
        getSettingsChoices();
    }

    private void hideNavigationBar(){
//        Log.println(Log.WARN, "Score Activity", "hideN");
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this, MainActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}

package com.example.slots.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.example.slots.R;

public class SplashActivity extends AppCompatActivity {

    private Runnable hideControls;
    private Handler threadHandler = new Handler();
    private int uiOptions;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_splash);

//        decorView = getWindow().getDecorView();
//        uiOptions =   View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        decorView.setOnSystemUiVisibilityChangeListener
//                (new View.OnSystemUiVisibilityChangeListener() {
//                    @Override
//                    public void onSystemUiVisibilityChange(int visibility) {
//                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
//                            // видно
//                            threadHandler.postDelayed(hideControls, 1000);
//                        }
//                    }
//                });
//
//        hideControls = new Runnable() {
//            @Override
//            public void run() {
//                hideAllControls();
//            }
//        };

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new LongOperation().execute();
    }//onCreate


//    //
    private void hideAllControls() {
        decorView.setSystemUiVisibility(uiOptions);
    }//hideAllControls
//
//
//    private void showControls() {
//        decorView.setSystemUiVisibility(View.VISIBLE);
//        threadHandler.removeCallbacks(hideControls);
//        threadHandler.postDelayed(hideControls, 3000);
//    }//showControls


    private class LongOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(10050);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return null;
        }//doInBackground

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }//onPostExecute
    }//class LongOperation
}//class SplashActivity
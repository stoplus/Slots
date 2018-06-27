package com.example.slots.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.slots.MyApp;
import com.example.slots.R;
import com.example.slots.entityRoom.GameData;
import com.example.slots.entityRoom.GameDataDao;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    GameDataDao gameDataDao;

    @BindView(R.id.slot_combination_1)
    ImageView comb1;
    @BindView(R.id.slot_combination_2)
    ImageView comb2;
    @BindView(R.id.slot_combination_3)
    ImageView comb3;
    @BindView(R.id.slot_combination_4)
    ImageView comb4;
    @BindView(R.id.slot_combination_5)
    ImageView comb5;
    @BindView(R.id.slot_combination_6)
    ImageView comb6;
    @BindView(R.id.slot_combination_7)
    ImageView comb7;
    @BindView(R.id.slot_combination_8)
    ImageView comb8;
    @BindView(R.id.slot_combination_9)
    ImageView comb9;

    @BindView(R.id.textViewCombinations)
    TextView textViewCombinations;
    @BindView(R.id.coins_descript)
    TextView coinsDescript;
    @BindView(R.id.textView_field_lines_descript)
    TextView textViewFieldLinesDescript;
    @BindView(R.id.field_bet_descript)
    TextView fieldBetDescript;

    @BindView(R.id.textView_jackpot)
    TextView jackpotField;
    @BindView(R.id.textView_coins)
    TextView coinsField;
    @BindView(R.id.textView_field_lines)
    TextView textViewFieldLines;
    @BindView(R.id.textView_field_bet)
    TextView betField;

    private Disposable disposable;
    private int[] combinationsIdImage = {R.drawable.combination_1, R.drawable.combination_2, R.drawable.combination_3,
            R.drawable.combination_4, R.drawable.combination_5, R.drawable.combination_6, R.drawable.combination_7};
    private ImageView[] combinationsImageViewId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MyApp.app().appComponent().inject(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        combinationsImageViewId = new ImageView[]{comb1, comb2, comb3, comb4, comb5, comb6, comb7, comb8, comb9};
        setFonts();
        getListUserData();
    }//onCreate


    public void getListUserData() {
        disposable = gameDataDao.getListAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listAccounts -> {
                    disposable.dispose();
                    if (listAccounts.size() > 0) {
                        betField.setText(String.valueOf(listAccounts.get(0).getBet()));
                        jackpotField.setText(String.valueOf(listAccounts.get(0).getJackpot()));
                        coinsField.setText(String.valueOf(listAccounts.get(0).getCoins()));
                    } else {
                        int bet = Integer.parseInt(betField.getText().toString());
                        int jackpot = Integer.parseInt(jackpotField.getText().toString());
                        int myCoins = Integer.parseInt(coinsField.getText().toString());
                        GameData gameData = new GameData(jackpot, myCoins, bet);
                        insertAccount(gameData);
                    }
                });
    }//getListImageObj


    private void insertAccount(final GameData gameData) {
        Completable.fromAction(() -> gameDataDao.insert(gameData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {//Вставляем новую
                        getListUserData();
                    }//onComplete

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }//addProductForList


    private void updateDB(final GameData gameData) {
        Completable.fromAction(() -> gameDataDao.update(gameData))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        getListUserData();
                    }//onComplete

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }//updateImageObj


    public void btnSpin(View view) {
        MyTask mt = new MyTask();
        mt.execute();
    }


    class MyTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 20; i > 0; i--) {
                    TimeUnit.MILLISECONDS.sleep(25);
                    publishProgress();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }//doInBackground

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            setImage();
        }//onProgressUpdate
    }//class MyTask


    private void setImage() {
        //вставляем случайные комбинации в ячейки спинеров
        for (int i = 0; i < combinationsImageViewId.length; i++) {
            int position = getRandom();
            int idImage = combinationsIdImage[position];//
            ImageView image = combinationsImageViewId[i];
            image.setImageResource(idImage);
        }
    }


    private int getRandom() {
        Random rand = new Random();
        return rand.nextInt(7);
    }// getRandom


    public void btnMinus(View view) {
        int bet = Integer.parseInt(betField.getText().toString());
        if (bet > 5) {
            bet -= 5;
            betField.setText(String.valueOf(bet));
        }
    }

    public void btnPlus(View view) {
        int bet = Integer.parseInt(betField.getText().toString());
        if (bet < 100) {
            bet += 5;
            betField.setText(String.valueOf(bet));
        }
    }

    public void btnSettings(View view) {
    }


    private void setFonts() {
        textViewCombinations.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        textViewFieldLinesDescript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        coinsDescript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        fieldBetDescript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        jackpotField.setTypeface(Typeface.createFromAsset(getAssets(), "Bodonio.ttf"));
    }//setFonts
}

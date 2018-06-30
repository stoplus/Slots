package com.example.slots.ui;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.slots.MyApp;
import com.example.slots.R;
import com.example.slots.adapters.Adapter;
import com.example.slots.entityRoom.GameData;
import com.example.slots.entityRoom.GameDataDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    GameDataDao gameDataDao;

    //description
    @BindView(R.id.textViewCombinations)
    TextView textViewCombinations;
    @BindView(R.id.coins_descript)
    TextView coinsDescript;
    @BindView(R.id.textView_field_lines_descript)
    TextView textViewFieldLinesDescript;
    @BindView(R.id.field_bet_descript)
    TextView fieldBetDescript;

    //set fields
    @BindView(R.id.textView_jackpot)
    TextView jackpotField;
    @BindView(R.id.textView_coins)
    TextView coinsField;
    @BindView(R.id.textView_field_lines)
    TextView textViewFieldLines;
    @BindView(R.id.textView_field_bet)
    TextView betField;

    @BindView(R.id.button_big)
    Button btnSpin;

    private Disposable disposable;
    private int[] combinationsIdImage = {R.drawable.combination_1, R.drawable.combination_2, R.drawable.combination_3,
            R.drawable.combination_4, R.drawable.combination_5, R.drawable.combination_6, R.drawable.combination_7};
    private int idAccount;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private final int SIZE = 20;//количество элементов в каждом спинере
    private List<Integer> tempList = new ArrayList<>();
    private List<Integer> tempList2 = new ArrayList<>();
    private List<Integer> tempList3 = new ArrayList<>();
    private boolean flag = false;
    private List<Integer> items = new ArrayList<>();
    private List<Integer> items2 = new ArrayList<>();
    private List<Integer> items3 = new ArrayList<>();
    private int count = 0;
    private static final float SPEED = 70f;// Speed sclol spiners (default=25f)
    private GameData gameDataTemp;
    private View view;
    private Snackbar snackbar;
    private TextView summ;
    private int win_summ = 0;
    BaseTransientBottomBar.ContentViewCallback ff;
    private int countLine;
    private int myCoins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(view);

        ButterKnife.bind(this);

        MyApp.app().appComponent().inject(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setupSpinners();

        setFonts();
        getListAllAccounts();
        setupSnackBar();
    }//onCreate


    private void setupSnackBar() {
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        View snackView = getLayoutInflater().inflate(R.layout.win_snack_bar, null);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        summ = snackView.findViewById(R.id.win_summ);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.TRANSPARENT);
        snackBarView.addView(snackView, 0);
        snackbar.setDuration(800);

        // запускаем анимацию для компонента listView
        ff = new BaseTransientBottomBar.ContentViewCallback() {
            @Override
            public void animateContentIn(int delay, int duration) {//вверх
                ViewCompat.setAlpha(snackView, 0f);
                ViewCompat.animate(snackView)
                        .withLayer()
                        .alphaBy(1f)
                        .setDuration(duration)
                        .setStartDelay(delay);
            }//animateContentIn

            @Override
            public void animateContentOut(int delay, int duration) {//вниз
            }
        };
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                btnSpin.setClickable(true);
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
        snackbar.isShown();
    }//setupSnackBar


    private void setupSpinners() {
        recyclerView = findViewById(R.id.recycler1);
        recyclerView2 = findViewById(R.id.recycler2);
        recyclerView3 = findViewById(R.id.recycler3);

        recyclerView.addOnItemTouchListener(listener);
        recyclerView2.addOnItemTouchListener(listener);
        recyclerView3.addOnItemTouchListener(listener);

        items = createLists(tempList);
        items2 = createLists(tempList2);
        items3 = createLists(tempList3);
        count = 0;

        recyclerView.setAdapter(Adapter.newInstance(this, items));
        recyclerView2.setAdapter(Adapter.newInstance(this, items2));
        recyclerView3.setAdapter(Adapter.newInstance(this, items3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }//setupSpinners


    private RecyclerView.OnItemTouchListener listener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };


    private List<Integer> createLists(List<Integer> tempListForSize) {
        List<Integer> list = new ArrayList<>();
        count++;
        if (tempListForSize.size() == 0) {//если первый раз запускали
            for (int i = 0; i < SIZE; i++) {
                int pos = addList(i);
                list.add(pos);//добавляем случайное id картинки
            }
        } else {//если не первый раз запускаем
            switch (count) {
                case 1:
                    items.clear();
                    list.addAll(tempList);
                    tempList.clear();
                    break;
                case 2:
                    items2.clear();
                    list.addAll(tempList2);
                    tempList2.clear();
                    break;
                case 3:
                    items3.clear();
                    list.addAll(tempList3);
                    tempList3.clear();
                    break;
            }//switch
            for (int i = 3; i < SIZE; i++) {

                int pos = addList(i);
                switch (count) {
                    case 1:
                        list.add(pos);//добавляем случайное id картинки
                        break;
                    case 2:
                        list.add(pos);//добавляем случайное id картинки
                        break;
                    case 3:
                        list.add(pos);//добавляем случайное id картинки
                        break;
                }//switch
            }//for
        }//if
        flag = false;
        return list;
    }//createList

    private int addList(int size) {
        int position = getRandom();
        //копируем последние три элемента
        if (size == SIZE - 3)
            flag = true;
        if (flag) {
            switch (count) {
                case 1:
                    tempList.add(combinationsIdImage[position]);
                    break;
                case 2:
                    tempList2.add(combinationsIdImage[position]);
                    break;
                case 3:
                    tempList3.add(combinationsIdImage[position]);
                    break;
            }//switch
        }//if
        return combinationsIdImage[position];
    }//addList


    public void getListAllAccounts() {
        disposable = gameDataDao.getListAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listAccounts -> {
                    disposable.dispose();
                    if (listAccounts.size() > 0) {
                        //заполняем поля из данных аккаунта
                        betField.setText(String.valueOf(listAccounts.get(0).getBet()));
                        jackpotField.setText(String.valueOf(listAccounts.get(0).getJackpot()));
                        coinsField.setText(String.valueOf(listAccounts.get(0).getCoins()));
                        textViewFieldLines.setText(String.valueOf(countLine));
                        idAccount = listAccounts.get(0).getId();
                        gameDataTemp = listAccounts.get(0);
                        if (win_summ > 0) {
                            summ.setText(String.valueOf(win_summ));
                            ff.animateContentIn(0, 400);//вверх
                            snackbar.show();
                            win_summ = 0;
                        } else {
                            btnSpin.setClickable(true);
                        }
                    } else {
                        //добавляем новый аккаунт, если аккаунт не создан
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
                        win_summ = 0;
                        getListAllAccounts();
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
                        getListAllAccounts();
                    }//onComplete

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }//updateImageObj


    private void updateBetAccount(int bet, int id) {
        Completable.fromAction(() -> gameDataDao.updateBet(bet, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        win_summ = 0;
                        getListAllAccounts();
                    }//onComplete

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }//updateImageObj


    public void btnSpin(View view) {
        items = createLists(tempList);
        items2 = createLists(tempList2);
        items3 = createLists(tempList3);
        count = 0;

        recyclerView.setAdapter(Adapter.newInstance(this, items));
        recyclerView2.setAdapter(Adapter.newInstance(this, items2));
        recyclerView3.setAdapter(Adapter.newInstance(this, items3));

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity.this) {
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity.this) {
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity.this) {
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
        recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount());
        recyclerView3.smoothScrollToPosition(recyclerView3.getAdapter().getItemCount());
        checkWin();
    }//btnSpin


    private void checkWin() {
//        btnSpin.setClickable(false);
//        int bet = Integer.parseInt(betField.getText().toString());
//        int jackpot = Integer.parseInt(jackpotField.getText().toString());
//        int myCoins = Integer.parseInt(coinsField.getText().toString());
//        boolean checkOtherLineFlag = false;
//
//        if (tempList.get(1).equals(tempList2.get(1)) && tempList.get(1).equals(tempList3.get(1))) {
//            switch (tempList2.get(1)) {
//                case R.drawable.combination_1:
//                    win_summ = bet * 10;
//                    myCoins += win_summ;
//                    break;
//                case R.drawable.combination_2:
//                    win_summ = bet * 15;
//                    myCoins += win_summ;
//                    break;
//                case R.drawable.combination_3:
//                    win_summ = bet * 25;
//                    myCoins += win_summ;
//                    break;
//                case R.drawable.combination_4:
//                    win_summ = bet * 35;
//                    myCoins += win_summ;
//                    break;
//                case R.drawable.combination_5:
//                    win_summ = bet * 50;
//                    myCoins += win_summ;
//                    break;
//                case R.drawable.combination_6:
//                    win_summ = bet * 75;
//                    myCoins += win_summ;
//                    break;
//                case R.drawable.combination_7:
//                    win_summ = jackpot;
//                    myCoins += win_summ;
//                    jackpot = 0;
//                    break;
//            }//switch
//            checkOtherLineFlag = true;
//        } else if (tempList.get(1).equals(R.drawable.combination_7) &&
//                tempList2.get(1).equals(R.drawable.combination_7) ||
//                tempList.get(1).equals(R.drawable.combination_7) &&
//                        tempList3.get(1).equals(R.drawable.combination_7) ||
//                tempList2.get(1).equals(R.drawable.combination_7) &&
//                        tempList3.get(1).equals(R.drawable.combination_7)) {
//            win_summ = bet * 50;
//            myCoins += win_summ;
//            checkOtherLineFlag = true;
//        } else if (tempList.get(1).equals(R.drawable.combination_7) ||
//                tempList2.get(1).equals(R.drawable.combination_7) ||
//                tempList3.get(1).equals(R.drawable.combination_7)) {
//            win_summ = bet * 25;
//            myCoins += win_summ;
//            checkOtherLineFlag = true;
//        } else {
//            myCoins -= bet;
//            jackpot += bet;
//        }
        countLine = 0;
        myCoins = Integer.parseInt(coinsField.getText().toString());
        boolean checkOtherLineFlag = checkLine(true, 1, 1, 1);
        if (checkOtherLineFlag) {
            countLine++;
            if (checkLine(false, 0, 0, 0)) {
                countLine++;
            }
            if (checkLine(false, 2, 2, 2)) {
                countLine++;
            }
            if (checkLine(false, 0, 1, 2)) {
                countLine++;
            }
            if (checkLine(false, 2, 1, 0)) {
                countLine++;
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDB(gameDataTemp);
            }
        }, 1000);
    }//checkWin

    private boolean checkLine(boolean isFirstLine, int comb1, int comb2, int comb3) {
        btnSpin.setClickable(false);
        int bet = Integer.parseInt(betField.getText().toString());
        int jackpot = Integer.parseInt(jackpotField.getText().toString());

        boolean checkOtherLineFlag = false;
        int winSummTemp = 0;
        int myCoinsTemp = 0;

        if (tempList.get(comb1).equals(tempList2.get(comb2)) && tempList.get(comb1).equals(tempList3.get(comb3))) {
            switch (tempList2.get(1)) {
                case R.drawable.combination_1:
                    winSummTemp = bet * 10;
                    myCoinsTemp += winSummTemp;
                    break;
                case R.drawable.combination_2:
                    winSummTemp = bet * 15;
                    myCoinsTemp += winSummTemp;
                    break;
                case R.drawable.combination_3:
                    winSummTemp = bet * 25;
                    myCoinsTemp += winSummTemp;
                    break;
                case R.drawable.combination_4:
                    winSummTemp = bet * 35;
                    myCoinsTemp += winSummTemp;
                    break;
                case R.drawable.combination_5:
                    winSummTemp = bet * 50;
                    myCoinsTemp += winSummTemp;
                    break;
                case R.drawable.combination_6:
                    winSummTemp = bet * 75;
                    myCoinsTemp += winSummTemp;
                    break;
                case R.drawable.combination_7:
                    if (isFirstLine) {
                        winSummTemp = jackpot;
                        myCoinsTemp += winSummTemp;
                        jackpot = 0;
                    }
                    break;
            }//switch
            checkOtherLineFlag = true;
        } else if ((tempList.get(1).equals(R.drawable.combination_7) &&
                tempList2.get(1).equals(R.drawable.combination_7) ||
                tempList.get(1).equals(R.drawable.combination_7) &&
                        tempList3.get(1).equals(R.drawable.combination_7) ||
                tempList2.get(1).equals(R.drawable.combination_7) &&
                        tempList3.get(1).equals(R.drawable.combination_7)) && isFirstLine) {
            winSummTemp = bet * 50;
            myCoinsTemp += winSummTemp;
            checkOtherLineFlag = true;
        } else if ((tempList.get(1).equals(R.drawable.combination_7) ||
                tempList2.get(1).equals(R.drawable.combination_7) ||
                tempList3.get(1).equals(R.drawable.combination_7)) && isFirstLine) {
            winSummTemp = bet * 25;
            myCoinsTemp += winSummTemp;
            checkOtherLineFlag = true;
        } else {
            if (isFirstLine){
                myCoins -= bet;
                jackpot += bet;
            }
        }
        if (winSummTemp > 0) {
            win_summ += winSummTemp;
            myCoins += myCoinsTemp;
        }
        gameDataTemp.setBet(bet);
        gameDataTemp.setCoins(myCoins);
        gameDataTemp.setJackpot(jackpot);

        return checkOtherLineFlag;
    }


    private int getRandom() {
        Random rand = new Random();
        return rand.nextInt(7);
    }// getRandom


    public void btnMinus(View view) {
        int bet = Integer.parseInt(betField.getText().toString());
        if (bet > 5) {
            bet -= 5;
            updateBetAccount(bet, idAccount);
        }
    }//btnMinus


    public void btnPlus(View view) {
        int bet = Integer.parseInt(betField.getText().toString());
        if (bet < 100) {
            bet += 5;
            updateBetAccount(bet, idAccount);
        }
    }//btnPlus


    public void btnSettings(View view) {
        View uboutUs = getLayoutInflater().inflate(R.layout.about_us, null);
        new AlertDialog.Builder(this)
                .setView(uboutUs)
                .create().show();
    }//btnSettings


    private void setFonts() {
        textViewCombinations.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        textViewFieldLinesDescript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        coinsDescript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        fieldBetDescript.setTypeface(Typeface.createFromAsset(getAssets(), "Sextan-Bold-FFP.ttf"));
        jackpotField.setTypeface(Typeface.createFromAsset(getAssets(), "Bodonio.ttf"));
    }//setFonts
}

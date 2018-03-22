package com.deepak.go_inte.reactor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepak.go_inte.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CredReactEx extends AppCompatActivity {

    private static final String TAG = CredReactEx.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiService apiService;
    private ArrayList<Credit> creditList = new ArrayList<>();
    private LinearLayout main_land;
    private CreditAdapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.react_layout);

        main_land = (LinearLayout)findViewById(R.id.main_land);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        apiService = ApiClient.getClient().create(ApiService.class);

        mAdapter = new CreditAdapter(this, creditList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ConnectableObservable<List<Credit>> ticketsObservable = getCredits().replay();
        disposable.add(
                ticketsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Credit>>() {

                            @Override
                            public void onNext(List<Credit> tickets) {
                                // Refreshing list
                                creditList.clear();
                                creditList.addAll(tickets);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                showError(e);
                            }

                            @Override
                            public void onComplete() {
                                System.err.println("Black Label>>"+creditList.size());
                            }
                        }));

        ticketsObservable.connect();
    }

    private Observable<List<Credit>> getCredits() {
        return apiService.getCredit()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void showError(Throwable e) {
        Log.e(TAG, "showError: " + e.getMessage());

        Snackbar snackbar = Snackbar
                .make(main_land, e.getMessage(), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}

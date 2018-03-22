package com.deepak.go_inte;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String api = "http://192.168.1.5/testApi/api.php";
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Credentials> credential;
    private ProgressDialog progressDialog;
    private CredentialAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        credential = new ArrayList<>();

        progressDialog = ProgressDialog
                .show(MainActivity.this, "", "Loading...");
        progressDialog.setCancelable(false);

        fetchCredentials();

        mAdapter = new CredentialAdapter(this, credential);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //newest to oldest order (database stores from oldest to newest)
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                fetchCredentials();
            }
        });

    }

    private void fetchCredentials() {
        JsonArrayRequest request = new JsonArrayRequest(api,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the credentials! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        progressDialog.dismiss();
                        // Stopping swipe refresh
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e("BLACK LABEL", response.toString());

                        List<Credentials> cred = new Gson().fromJson(response.toString(), new TypeToken<List<Credentials>>() {
                        }.getType());

                        // adding recipes to cart list
                        credential.clear();
                        credential.addAll(cred);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onRefresh() {

        // Fetching data from server
        fetchCredentials();
    }

}

package com.example.geomhelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.geomhelper.R;
import com.example.geomhelper.resources.RVLeaderboardAdapter;
import com.example.geomhelper.retrofit.User;
import com.example.geomhelper.retrofit.UserService;
import com.example.geomhelper.sqlite.DB;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.geomhelper.sqlite.OpenHelper.NUM_DESC;

public class FragmentLeaderboard extends Fragment {

    public FragmentLeaderboard() {
    }

    private RecyclerView recyclerView;
    private RVLeaderboardAdapter rvLeaderboardAdapter;
    private ProgressBar progressBar;
    private List<User> users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        RelativeLayout relativeLayout = rootView.findViewById(R.id.frame_leaderboard);

        users = new ArrayList<>();

        rvLeaderboardAdapter = new RVLeaderboardAdapter(getContext(), users, false);

        recyclerView = rootView.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rvLeaderboardAdapter);
        recyclerView.setVisibility(View.INVISIBLE);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar, params);
        progressBar.setVisibility(View.VISIBLE);

        BottomNavigationView bottomNavigationView = Objects.requireNonNull(getActivity()).
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.navigation_leaderboard) {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }
                });

        DB db = new DB(getContext());
        int desc = db.getInt(NUM_DESC);
        if (desc < 10)
            desc = 10;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(User.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        UserService userService = retrofit.create(UserService.class);
        userService.getLeaders(desc).enqueue(new Callback<String>() {
                    String result;

                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                result = response.body();
                                result = Objects.requireNonNull(result).replace("[", "");
                                result = result.replace("]", "");
                                String[] split = result.split(",");
                                Gson gson = new Gson();
                                StringBuilder q = new StringBuilder();
                                int a = 0;
                                for (String aSplit : split) {
                                    if (aSplit.contains("}")) q.append(aSplit);
                                    else q.append(aSplit).append(",");
                                    a++;
                                    if (aSplit.contains("}")) {
                                        users.add(gson.fromJson(q.toString(), User.class));
                                        a = 0;
                                        q = new StringBuilder();
                                    }
                                }

                                rvLeaderboardAdapter.setData(users);
                                rvLeaderboardAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    }
                });

        return rootView;
    }

}
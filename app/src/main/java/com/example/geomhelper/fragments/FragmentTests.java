package com.example.geomhelper.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.activities.MainActivity;
import com.example.geomhelper.content.Test;
import com.example.geomhelper.content.Tests;
import com.example.geomhelper.retrofit.TestJSON;
import com.example.geomhelper.sqlite.DB;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_TESTS;

public class FragmentTests extends Fragment {

    public FragmentTests() {
    }

    public static boolean fab = false;
    private RecyclerView recyclerView;
    private FragmentManager fragmentManager;
    private RVAdpater rvTestsAdapter;
    private LinearLayoutManager verticalManager, horizontalManager;
    private List<Test> currentTests;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tests, container, false);

        fab = false;

        currentTests = new Tests().getCurrentTests();

        verticalManager = new LinearLayoutManager(getContext());
        horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvTestsAdapter = new RVAdpater(currentTests);

        fragmentManager = getFragmentManager();

        recyclerView = rootView.findViewById(R.id.rv_tests);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setAdapter(rvTestsAdapter);
        recyclerView.scrollToPosition(currentTests.size() - 1);

        BottomNavigationView bottomNavigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                recyclerView.smoothScrollToPosition(rvTestsAdapter.getItemCount() - 1);
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_grow);

        floatingActionButton = rootView.findViewById(R.id.fab_tests);
        floatingActionButton.startAnimation(animation);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();

                DB db = new DB(getContext());

                TestJSON testJSON = new Gson().fromJson(
                        db.getString(NUM_COLUMN_TESTS), TestJSON.class);
                if (testJSON == null) {
                    Toast.makeText(getContext(), R.string.you_havent_pass_tests,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // if(testJSON.getTestsMap())
                    fab = true;

                    List<Test> tests = new Tests().getCurrentTests();

                    FragmentFirstTask.fabTest = tests.size() - 1 - r.nextInt(testJSON.getTests().size());
                    FragmentFirstTask.fabTheme = r.nextInt(testJSON.getTestsMap(
                            FragmentFirstTask.fabTest).size());
                    FragmentFirstTask.fabStage = r.nextInt(testJSON.getTest(
                            FragmentFirstTask.fabTest, FragmentFirstTask.fabTheme));

                    FragmentSecondTask.fabTest = tests.size() - 1 - r.nextInt(testJSON.getTests().size());
                    FragmentSecondTask.fabTheme = r.nextInt(testJSON.getTestsMap(
                            FragmentSecondTask.fabTest).size());
                    FragmentSecondTask.fabStage = r.nextInt(testJSON.getTest(
                            FragmentSecondTask.fabTest, FragmentSecondTask.fabTheme));

                    FragmentThirdTask.fabTest = tests.size() - 1 - r.nextInt(testJSON.getTests().size());
                    FragmentThirdTask.fabTheme = r.nextInt(testJSON.getTestsMap(
                            FragmentThirdTask.fabTest).size());
                    FragmentThirdTask.fabStage = r.nextInt(testJSON.getTest(
                            FragmentThirdTask.fabTest, FragmentThirdTask.fabTheme));

                    recyclerView.setVisibility(View.INVISIBLE);
                    recyclerView.setClickable(false);
                    floatingActionButton.hide();

                    MainActivity.back = 3;
                    Person.backTests = 3;
                    FragmentTransaction fragmentTransaction =
                            Objects.requireNonNull(getFragmentManager()).beginTransaction();
                    FragmentFirstTask fragmentFirstTask = new FragmentFirstTask();
                    fragmentTransaction.replace(R.id.frame_tests, fragmentFirstTask);
                    fragmentTransaction.commit();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(horizontalManager);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(verticalManager);
        }
    }

    @Override
    public void onStart() {
        if (MainActivity.back == 3) {
            floatingActionButton.setVisibility(View.INVISIBLE);
            floatingActionButton.setClickable(false);
            recyclerView.setVisibility(View.INVISIBLE);
            recyclerView.setClickable(false);
        }
        super.onStart();
    }

    class RVAdpater extends RecyclerView.Adapter<RVAdpater.TestViewHolder> {

        private List<Test> tests;

        public void setData(List<Test> t) {
            tests = t;
        }

        RVAdpater(List<Test> tests) {
            this.tests = tests;
        }

        @NonNull
        @Override
        public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_tests_content, parent, false);
            return new TestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {

            final Test test = tests.get(position);
            holder.test = test;

            holder.imageView.setBackgroundResource(test.getBackground());
            holder.name.setText(test.getTestName());

        }

        @Override
        public int getItemCount() {
            return tests.size();
        }

        class TestViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            ImageView imageView;
            CardView cardView;
            Test test;

            public void setTest(Test test) {
                this.test = test;
            }

            TestViewHolder(final View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.text_rv_tests);
                imageView = itemView.findViewById(R.id.card_tests_image);
                cardView = itemView.findViewById(R.id.card_tests);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Person.currentTest = currentTests.indexOf(test);
                        MainActivity.back = 3;
                        Person.backTests = 3;
                        floatingActionButton.hide();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                        fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
                        fragmentTransaction.commit();
                        recyclerView.setVisibility(View.INVISIBLE);
                        recyclerView.setClickable(false);
                    }
                });
            }

        }

    }

}

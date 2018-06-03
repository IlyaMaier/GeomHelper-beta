package com.example.geomhelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.content.Achievement;
import com.example.geomhelper.content.Achievements;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.geomhelper.Person.pref;

public class FragmentAchievements extends Fragment {

    public FragmentAchievements() {
    }

    View view;
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    List<Achievement> achievements;
    public static int coursesE = 10, testsE = 20, fabsE = 30, xpE = 40, sharedE = 50;
    public static int courses = 4, tests = 2, fabs = 5, xp = 50, shared = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_achievements, container, false);

        Achievements achievs = new Gson().fromJson(
                pref.getString("achievements", ""), Achievements.class);
        if (achievs == null)
            achievs = new Achievements();

        int coursesM = 0, testsM = 0, fabsM = 0, xpM = 0, sharedM = 0;
        if (achievs.isCoursesB()) coursesM = courses;
        else coursesM = achievs.getCourses();
        if (achievs.isTestsB()) testsM = tests;
        else testsM = achievs.getTests();
        if (achievs.isFabsB()) fabsM = fabs;
        else fabsM = achievs.getFabs();
        if (achievs.isXp()) xpM = xp;
        else xpM = Person.experience;
        if (achievs.isSharedB()) sharedM = shared;
        else sharedM = achievs.getShared();

        achievements = new ArrayList<>();
        achievements.add(new Achievement(R.drawable.courses, coursesM,
                4, "Добавь курсы: 4", "10 xp"));
        achievements.add(new Achievement(R.drawable.tests, testsM,
                2, "Пройди тесты: 2", "20 xp"));
        achievements.add(new Achievement(R.drawable.dumb, fabsM,
                5, "Пройди тесты в тренажере: 5", "30 xp"));
        achievements.add(new Achievement(R.drawable.tests, xpM, 50,
                "Набери опыт: 50", "40 xp"));
        achievements.add(new Achievement(android.R.drawable.ic_menu_share, sharedM,
                1, "Поделись приложением", "50 xp"));

        recyclerView = view.findViewById(R.id.rv_achiev);
        rvAdapter = new RVAdapter();
        rvAdapter.setAchievements(achievements);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.AchievHolder> {

        private List<Achievement> achievements;

        public void setAchievements(List<Achievement> achievements) {
            this.achievements = achievements;
        }

        public RVAdapter() {
        }

        @NonNull
        @Override
        public AchievHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_achiev,
                    parent, false);
            return new AchievHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AchievHolder holder, int position) {
            Achievement a = achievements.get(position);

            holder.img.setBackgroundResource(a.getImage());
            holder.name.setText(a.getName());
            holder.bonus.setText(a.getBonus());
            String p = a.getProgress() + "/" + a.getMax();
            holder.progress.setText(p);
            holder.progressBar.setMax(a.getMax());
            holder.progressBar.setProgress(a.getProgress());
        }

        @Override
        public int getItemCount() {
            return achievements.size();
        }

        class AchievHolder extends RecyclerView.ViewHolder {

            TextView name, bonus, progress;
            ImageView img;
            ProgressBar progressBar;

            public AchievHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.text_achiev1);
                bonus = itemView.findViewById(R.id.text_achiev2);
                progress = itemView.findViewById(R.id.text_achiev3);
                img = itemView.findViewById(R.id.image_achiev);
                progressBar = itemView.findViewById(R.id.progress_achiev);
            }

        }

    }

}

package com.example.geomhelper.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.activities.MainActivity;
import com.example.geomhelper.content.Achievements;
import com.example.geomhelper.content.FirstTask;
import com.example.geomhelper.content.FirstTasks;
import com.example.geomhelper.content.Levels;
import com.example.geomhelper.content.SecondTask;
import com.example.geomhelper.content.SecondTasks;
import com.example.geomhelper.content.Test;
import com.example.geomhelper.content.Tests;
import com.example.geomhelper.content.ThirdTask;
import com.example.geomhelper.content.ThirdTasks;
import com.example.geomhelper.retrofit.TestJSON;
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

import static com.example.geomhelper.Person.task;
import static com.example.geomhelper.fragments.FragmentTests.fab;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_ACHIEVEMENTS;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_TESTS;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_ACHIEVEMENTS;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_TESTS;

public class FragmentResult extends Fragment {

    private FirstTask firstTask;
    private SecondTask secondTask;
    private ThirdTask thirdTask;
    public static int answer1 = -1;
    public static String answer2 = "";
    public static String[] answers3 = new String[3];
    private int xp;

    public FragmentResult() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        DB db = new DB(getContext());

        initializeTests();

        LinearLayout linearLayout = rootView.findViewById(R.id.ll_result);

        CardView card1 = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_result, null);
        CardView card2 = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_result, null);
        CardView card3 = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_result, null);

        TextView textView = card1.findViewById(R.id.text_number);
        textView.setText("1");
        textView = card2.findViewById(R.id.text_number);
        textView.setText("2");
        textView = card3.findViewById(R.id.text_number);
        textView.setText("3");

        textView = card1.findViewById(R.id.text_answer);
        StringBuilder answer = new StringBuilder(getString(R.string.correct_answer) + firstTask.getCorrectAnswer());
        textView.setText(answer.toString());
        textView = card2.findViewById(R.id.text_answer);
        answer = new StringBuilder(getString(R.string.correct_answer) + secondTask.getAnswer());
        textView.setText(answer.toString());
        textView = card3.findViewById(R.id.text_answer);
        answer = new StringBuilder(getString(R.string.correct_answers));
        for (int i = 0; i < thirdTask.getAnswer().length; i++)
            answer.append(thirdTask.getAnswer()[i]).append(", ");
        answer.replace(answer.length() - 2, answer.length() - 1, " ");
        textView.setText(answer.toString());

        if (firstTask.getCorrectAnswer() == answer1) {
            ImageView imageView = card1.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.correct);
            if (fab)
                xp += 1;
            else xp += 2;
        } else if (answer1 == -1) {
            ImageView imageView = card1.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.unchecked);
        } else {
            ImageView imageView = card1.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.wrong);
        }
        if (secondTask.getAnswer().equals(answer2)) {
            ImageView imageView = card2.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.correct);
            if (fab)
                xp += 2;
            else xp += 4;
        } else if (answer2.equals("")) {
            ImageView imageView = card2.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.unchecked);
        } else {
            ImageView imageView = card2.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.wrong);
        }
        if (thirdTask.getAnswer()[0].equals(answers3[0])
                && thirdTask.getAnswer()[1].equals(answers3[1])
                && thirdTask.getAnswer()[2].equals(answers3[2])) {
            ImageView imageView = card3.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.correct);
            if (fab)
                xp += 3;
            else xp += 6;
        } else if (answers3[0].equals("") || answers3[1].equals("") || answers3[2].equals("")) {
            ImageView imageView = card3.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.unchecked);
        } else {
            ImageView imageView = card3.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.wrong);
        }

        answer1 = -1;
        answer2 = "";
        answers3 = new String[3];

        linearLayout.addView(card1);
        linearLayout.addView(card2);
        linearLayout.addView(card3);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) card1.getLayoutParams();
        params1.setMargins(0, 30, 0, 0);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) card2.getLayoutParams();
        params2.setMargins(0, 30, 0, 0);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) card3.getLayoutParams();
        params3.setMargins(0, 30, 0, 15);

        TextView textXP = rootView.findViewById(R.id.experience);
        String t = "+" + xp + "xp";
        textXP.setText(t);
        Person.experience += xp;
        Person.currentLevel = new Levels().getLevel(Person.experience);
        Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);

        if (xp > 0 && !fab) {
            Achievements achievements = new Gson().fromJson(
                    db.getString(NUM_COLUMN_ACHIEVEMENTS), Achievements.class);
            if (achievements == null)
                achievements = new Achievements();
            achievements.setTests(achievements.getTests() + 1);

            if (achievements.getTests() >= FragmentAchievements.tests
                    && !achievements.isTestsB()) {
                achievements.setTestsB(true);
                Person.experience += FragmentAchievements.testsE;
                NotificationManager notificationManager = (NotificationManager)
                        Objects.requireNonNull(getContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "0")
                        .setSmallIcon(R.drawable.ic_menu_leaderboard)
                        .setColor(getResources().getColor(R.color.leaderboard))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.achievement_done) +
                                FragmentAchievements.testsE + "xp")
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true);
                Notification notification = builder.build();
                Objects.requireNonNull(notificationManager).notify(0, notification);
            }
            db.putString(COLUMN_ACHIEVEMENTS, new Gson().toJson(
                    achievements, Achievements.class));
        } else if (xp > 0) {
            Achievements achievements = new Gson().fromJson(
                    db.getString(NUM_COLUMN_ACHIEVEMENTS), Achievements.class);
            if (achievements == null) achievements = new Achievements();
            achievements.setFabs(achievements.getFabs() + 1);

            if (achievements.getFabs() >= FragmentAchievements.fabs
                    && !achievements.isFabsB()) {
                achievements.setFabsB(true);
                Person.experience += FragmentAchievements.fabsE;
                NotificationManager notificationManager = (NotificationManager)
                        Objects.requireNonNull(getContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "0")
                        .setSmallIcon(R.drawable.ic_menu_leaderboard)
                        .setColor(getResources().getColor(R.color.leaderboard))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.achievement_done) +
                                FragmentAchievements.fabsE + "xp")
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true);
                Notification notification = builder.build();
                Objects.requireNonNull(notificationManager).notify(0, notification);
            }
            db.putString(COLUMN_ACHIEVEMENTS, new Gson().toJson(
                    achievements, Achievements.class));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(User.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        try {
            UserService userService = retrofit.create(UserService.class);
            userService.updateUser(Person.uId, "experience", String.valueOf(Person.experience))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            try {
                                Toast.makeText(getContext(),
                                        R.string.can_not_send_data, Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            userService.updateUser(Person.uId, "achievements",
                    db.getString(NUM_COLUMN_ACHIEVEMENTS))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (Objects.requireNonNull(response.body()).equals("0"))
                                Toast.makeText(getContext(), R.string.can_not_send_data,
                                        Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            try {
                                Toast.makeText(getContext(),
                                        R.string.can_not_send_data,
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (NullPointerException e) {
            try {
                Toast.makeText(getContext(),
                        R.string.can_not_send_data,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }

        if (xp > 0 && !fab) {
            TestJSON testJSON = new Gson().fromJson(
                    db.getString(NUM_COLUMN_TESTS), TestJSON.class);
            if (testJSON == null) testJSON = new TestJSON();
            testJSON.setTest(Person.currentTest, Person.currentTestTheme, task + 1);
            db.putString(COLUMN_TESTS, new Gson().toJson(testJSON, TestJSON.class));
            try {
                UserService userService = retrofit.create(UserService.class);
                userService.updateUser(Person.uId, "tests", new Gson().toJson(testJSON, TestJSON.class))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                try {
                                    Toast.makeText(getContext(),
                                            R.string.can_not_send_data,
                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (NullPointerException e) {
                try {
                    Toast.makeText(getContext(),
                            R.string.can_not_send_data,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        }

        rootView.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab) {
                    MainActivity.back = 0;
                    Person.backTests = 0;
                    FragmentTransaction fragmentTransaction = Objects.requireNonNull(
                            getFragmentManager()).beginTransaction();
                    FragmentTests fragmentTests = new FragmentTests();
                    fragmentTransaction.replace(R.id.frame_tests, fragmentTests);
                    fragmentTransaction.commit();
                } else {
                    MainActivity.back = 3;
                    Person.backTests = 3;
                    FragmentTransaction fragmentTransaction = Objects.requireNonNull(
                            getFragmentManager()).beginTransaction();
                    FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                    fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
                    fragmentTransaction.commit();
                }
            }
        });
        return rootView;
    }

    private void initializeTests() {
        List<Test> tests = new Tests().getCurrentTests();

        if (fab) {
            firstTask = new FirstTasks().getTasks(tests.size() - 1 -
                    FragmentFirstTask.fabTest, FragmentFirstTask.fabTheme)
                    .get(FragmentFirstTask.fabStage);
            secondTask = new SecondTasks().getTasks(tests.size() - 1 -
                    FragmentSecondTask.fabTest, FragmentSecondTask.fabTheme)
                    .get(FragmentSecondTask.fabStage);
            thirdTask = new ThirdTasks().getTasks(tests.size() - 1 -
                    FragmentThirdTask.fabTest, FragmentThirdTask.fabTheme)
                    .get(FragmentThirdTask.fabStage);
        } else {
            ArrayList<FirstTask> firstTasks = new FirstTasks().
                    getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
            firstTask = firstTasks.get(task);

            ArrayList<SecondTask> secondTasks = new SecondTasks().
                    getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
            secondTask = secondTasks.get(task);

            ArrayList<ThirdTask> thirdTasks = new ThirdTasks().
                    getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
            thirdTask = thirdTasks.get(task);
        }
    }

}

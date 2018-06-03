package com.example.geomhelper.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.content.Course;
import com.example.geomhelper.content.Courses;
import com.example.geomhelper.fragments.FragmentCourses;
import com.example.geomhelper.fragments.FragmentDefinitions;
import com.example.geomhelper.fragments.FragmentLeaderboard;
import com.example.geomhelper.fragments.FragmentProfile;
import com.example.geomhelper.fragments.FragmentSettings;
import com.example.geomhelper.fragments.FragmentTestThemes;
import com.example.geomhelper.fragments.FragmentTests;
import com.example.geomhelper.fragments.FragmentThemes;
import com.example.geomhelper.fragments.FragmentTheorems;
import com.example.geomhelper.resources.ShowNotification;

import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Person.pref;

public class MainActivity extends AppCompatActivity {

    public static int back = 0;
    final int NUM_PAGES = 5;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    static SharedPreferences.Editor editor;
    boolean backCourses = false, backTests = false;
    List<Course> courses;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tests:
                    bottomNavigationView.setBackgroundResource(R.drawable.bnv_tests);
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    }
                    return true;
                case R.id.navigation_courses:
                    bottomNavigationView.setBackgroundResource(R.drawable.bnv_courses);
                    if (viewPager.getCurrentItem() != 1) {
                        viewPager.setCurrentItem(1);
                    }
                    return true;
                case R.id.navigation_profile:
                    bottomNavigationView.setBackgroundResource(R.drawable.bnv_profile);
                    if (viewPager.getCurrentItem() != 2) {
                        viewPager.setCurrentItem(2);
                    }
                    return true;
                case R.id.navigation_leaderboard:
                    bottomNavigationView.setBackgroundResource(R.drawable.bnv_leaders);
                    if (viewPager.getCurrentItem() != 3) {
                        viewPager.setCurrentItem(3);
                    }
                    return true;
                case R.id.navigation_settings:
                    bottomNavigationView.setBackgroundResource(R.drawable.bnv_settings);
                    if (viewPager.getCurrentItem() != 4) {
                        viewPager.setCurrentItem(4);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!pref.getBoolean(Person.APP_PREFERENCES_WELCOME, false)) {
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
        }
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            switch (pref.getString("pref_day_night", "")) {
                case "Включен":
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                    break;
                case "Выключен":
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                    break;
                case "Авто":
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_AUTO);
                    recreate();
                    break;
            }
        }

        try {
            Objects.requireNonNull(getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);

        courses = new Courses().getCurrentCourses();

        getWindow().setBackgroundDrawable(null);

        if (pref.getBoolean(Person.APP_PREFERENCES_WELCOME, false)) {
            Person.name = pref.getString(Person.APP_PREFERENCES_NAME, "Произошла ошибка");
            Person.id = pref.getString(Person.APP_PREFERENCES_UID, "-1");
            Person.currentLevel = pref.getString(Person.APP_PREFERENCES_LEVEL, "Произошла ошибка");
            Person.experience = pref.getInt(Person.APP_PREFERENCES_EXPERIENCE, -1);
            Person.currentLevelExperience = pref.getInt(Person.APP_PREFERENCES_LEVEL_EXPERIENCE, -1);
            Person.leaderBoardPlace = pref.getLong(Person.APP_PREFERENCES_LEADERBOARDPLACE, -1);
            Person.c = pref.getString("c", "");

            for (int i = 0; i < pref.getInt(Person.APP_PREFERENCES_COURSES_SIZE, 0); i++) {
                if (Person.courses.size() != pref.getInt(Person.APP_PREFERENCES_COURSES_SIZE, 0)) {
                    String course = Person.APP_PREFERENCES_COURSES + String.valueOf(i);
                    for (int j = 0; j < courses.size(); j++) {
                        if (pref.getString(course, "").equals(courses.get(j).getCourseName())) {
                            Person.courses.add(i, courses.get(j));
                        }
                    }
                }
            }
        } else {
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
        }

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(true, new FadePageTransformer());
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (!pref.getBoolean("fragment_settings", false)) {
            viewPager.setCurrentItem(2);
            bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
            bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            bottomNavigationView.setBackgroundResource(R.drawable.bnv_profile);
        } else {
            viewPager.setCurrentItem(4);
            bottomNavigationView.getMenu().findItem(R.id.navigation_settings).setChecked(true);
            bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            bottomNavigationView.setBackgroundResource(R.drawable.bnv_settings);
            editor = pref.edit();
            editor.putBoolean("fragment_settings", false);
            editor.apply();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_tests);
                        backTests = true;
                        backCourses = false;
                        back = Person.backTests;
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_courses);
                        backCourses = true;
                        backTests = false;
                        back = Person.backCourses;
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                        back = 0;
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);
                        back = 0;
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
                        back = 0;
                        break;
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        Intent notificationIntent = new Intent(getApplicationContext(), ShowNotification.class);
        PendingIntent contentIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(contentIntent);
            am.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + AlarmManager.INTERVAL_DAY * 2,
                    contentIntent);
        }

    }

    @Override
    public void onBackPressed() {
        if (FragmentCourses.h) {
            FragmentCourses.h = false;
            return;
        }
        if (back == 0) {
            if (viewPager.getCurrentItem() == 2) {
                super.onBackPressed();
            } else if (viewPager.getCurrentItem() >= 3) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        } else if (back == 1 && backCourses) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentCourses()).commit();
            Person.backCourses = 0;
            back = 0;
        } else if (back == 2 && backCourses) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentThemes()).commit();
            Person.backCourses = 1;
            back = 1;
        } else if (back == 3 && backTests) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_tests, new FragmentTests()).commit();
            back = 0;
        } else if (back == 4 && backTests) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_tests, new FragmentTestThemes()).commit();
            back = 3;
        } else if (back == 6) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_profile, new FragmentProfile()).commit();
            back = 0;
        } else if (back == 7) {
            FragmentDefinitions.h = false;
        } else if (back == 8) {
            FragmentTheorems.h = false;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentTests();
                case 1:
                    return new FragmentCourses();
                case 2:
                    return new FragmentProfile();
                case 3:
                    return new FragmentLeaderboard();
                case 4:
                    return new FragmentSettings();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAll(true, Person.pref.getBoolean(Person.APP_PREFERENCES_WELCOME, false));
    }

    public static void saveAll(boolean d, boolean welcome) {
        editor = pref.edit();
        editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
        editor.putString(Person.APP_PREFERENCES_UID, Person.id);
        editor.putString(Person.APP_PREFERENCES_LEVEL, Person.currentLevel);
        editor.putInt(Person.APP_PREFERENCES_EXPERIENCE, Person.experience);
        editor.putInt(Person.APP_PREFERENCES_LEVEL_EXPERIENCE, Person.currentLevelExperience);
        editor.putLong(Person.APP_PREFERENCES_LEADERBOARDPLACE, Person.leaderBoardPlace);
        editor.putInt(Person.APP_PREFERENCES_COURSES_SIZE, Person.courses.size());
        editor.putString("c", Person.c);
        if (d) editor.putBoolean("image", false);
        if (d) editor.putBoolean(Person.APP_PREFERENCES_WELCOME, welcome);
        for (int i = 0; i < Person.courses.size(); i++) {
            String course = Person.APP_PREFERENCES_COURSES + String.valueOf(i);
            editor.putString(course, Person.courses.get(i).getCourseName());
        }
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRestart();
    }

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(@NonNull View view, float position) {
            if (position <= -1.0F || position >= 1.0F) {
                view.setAlpha(0.0F);
                view.setVisibility(View.GONE);
            } else if (position == 0.0F) {     // [0]
                view.setAlpha(1.0F);
                view.setVisibility(View.VISIBLE);
            } else {
                view.setAlpha(1.0F - Math.abs(position));
                view.setTranslationX(-position * (view.getWidth() / 2));
                view.setVisibility(View.VISIBLE);
            }

        }

    }

}

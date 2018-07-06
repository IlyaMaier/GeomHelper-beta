package com.example.geomhelper.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.example.geomhelper.sqlite.DB;

import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Person.APP_PREFERENCES;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_C;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_EXPERIENCE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_LEADERBOARDPLACE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_LEVEL;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_LEVEL_EXPERIENCE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_NAME;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_SETTINGS;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_UID;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_C;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_DAY_NIGHT;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_EXPERIENCE;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_LEADERBOARDPLACE;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_LEVEL;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_LEVEL_EXPERIENCE;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_NAME;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_UID;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_SETTINGS;

public class MainActivity extends AppCompatActivity {

    public static int back = 0;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private boolean backCourses = false, backTests = false;
    private DB db;

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
        Person.id = getSharedPreferences(APP_PREFERENCES,
                Context.MODE_PRIVATE).getLong("id", -1);
        if (Person.id == -1) {
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
        }

        db = new DB(getApplicationContext());

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            switch (db.getString(NUM_COLUMN_DAY_NIGHT)) {
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

        List<Course> courses = new Courses().getCurrentCourses();

        getWindow().setBackgroundDrawable(null);

        if (Person.id != -1) {
            Person.name = db.getString(NUM_COLUMN_NAME);
            Person.uId = db.getString(NUM_COLUMN_UID);
            Person.currentLevel = db.getString(NUM_COLUMN_LEVEL);
            Person.experience = db.getInt(NUM_COLUMN_EXPERIENCE);
            Person.currentLevelExperience = db.getInt(NUM_COLUMN_LEVEL_EXPERIENCE);
            Person.leaderBoardPlace = db.getInt(NUM_COLUMN_LEADERBOARDPLACE);
            Person.c = db.getString(NUM_COLUMN_C);
            if (Person.c.equals("{}"))
                Person.c = "";
            for (int i = 0; i < Person.c.length(); i++) {
                if (!Person.courses.contains(courses.get(Integer.valueOf(Person.c.charAt(i) + ""))))
                    Person.courses.add(courses.get(Integer.valueOf(Person.c.charAt(i) + "")));
            }
        } else {
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
        }

        viewPager = findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(true, new FadePageTransformer());
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (db.getInt(NUM_SETTINGS) == 0) {
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
            db.putInt(COLUMN_SETTINGS, 0);
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
            return 5;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAll();
    }

    public void saveAll() {
        db.putString(COLUMN_NAME, Person.name);
        db.putString(COLUMN_UID, Person.uId);
        db.putString(COLUMN_LEVEL, Person.currentLevel);
        db.putInt(COLUMN_EXPERIENCE, Person.experience);
        db.putInt(COLUMN_LEVEL_EXPERIENCE, Person.currentLevelExperience);
        db.putInt(COLUMN_LEADERBOARDPLACE, Person.leaderBoardPlace);
        db.putString(COLUMN_C, Person.c);
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
            } else if (position == 0.0F) {
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

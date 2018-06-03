package com.example.geomhelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.geomhelper.R;
import com.example.geomhelper.fragments.FragmentStart;
import com.example.geomhelper.fragments.FragmentStart2;
import com.example.geomhelper.fragments.FragmentStart3;
import com.example.geomhelper.fragments.FragmentStart4;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    Button button;
    PageIndicatorView pageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        try {
            Objects.requireNonNull(getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        viewPager = findViewById(R.id.pager_start);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        button = findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(4);
        pageIndicatorView.setSelection(0);
        pageIndicatorView.setAnimationType(AnimationType.DROP);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentStart();
                case 1:
                    return new FragmentStart2();
                case 2:
                    return new FragmentStart3();
                case 3:
                    return new FragmentStart4();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    @Override
    public void onBackPressed() {

    }

}

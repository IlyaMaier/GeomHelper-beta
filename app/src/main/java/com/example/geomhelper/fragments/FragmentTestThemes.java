package com.example.geomhelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.activities.MainActivity;
import com.example.geomhelper.content.Test;
import com.example.geomhelper.content.Tests;
import com.example.geomhelper.resources.Resources;

import java.util.Objects;

public class FragmentTestThemes extends Fragment {

    private Resources resources = new Resources();

    public FragmentTestThemes() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_test_themes, container, false);

        Test test = new Tests().getCurrentTests().get(Person.currentTest);

        LinearLayout linearLayout = rootView.findViewById(R.id.linearLayoutFragmentTestThemes);
        Button[] button = new Button[test.getNumberOfThemes()];

        for (int i = 0; i < button.length; i++) {
            button[i] = new Button(getContext());
            button[i].setHeight(250);
            button[i].setBackgroundColor(resources.colorsTests[i]);
            button[i].setText(test.getTheme(i));
            button[i].setTextSize(16);
            button[i].setId(i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.back = 4;
                    Person.backTests = 4;
                    Person.currentTestTheme = v.getId();
                    FragmentTransaction fragmentTransaction =
                            Objects.requireNonNull(getFragmentManager()).beginTransaction();
                    FragmentFirstTask fragmentFirstTask = new FragmentFirstTask();
                    fragmentTransaction.replace(R.id.frame_tests, fragmentFirstTask);
                    fragmentTransaction.commit();
                }
            });
        }

        return rootView;

    }

}

package com.example.geomhelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.content.Test;
import com.example.geomhelper.content.Tests;
import com.example.geomhelper.content.ThirdTask;
import com.example.geomhelper.content.ThirdTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Person.task;
import static com.example.geomhelper.fragments.FragmentResult.answers3;
import static com.example.geomhelper.fragments.FragmentTests.fab;

public class FragmentThirdTask extends Fragment {

    private TextView textViewT31, textViewT32, textViewT33;
    private Button buttonEnd;
    private ImageView imageView3;
    private EditText editText31, editText32, editText33;
    private ThirdTask thirdTask;
    static int fabTest = 0, fabTheme = 0, fabStage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third_task, container, false);

        initializeTests();
        initializeViews(rootView);
        setInfo();

        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answers3[0] = editText31.getText().toString();
                answers3[1] = editText32.getText().toString();
                answers3[2] = editText33.getText().toString();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction().
                        replace(R.id.frame_tests, new FragmentResult()).commit();
            }
        });


        return rootView;
    }

    private void initializeTests() {
        List<Test> tests = new Tests().getCurrentTests();

        if (fab)
            thirdTask = new ThirdTasks().getTasks(tests.size() - 1 - fabTest, fabTheme).get(fabStage);
        else {
            ArrayList<ThirdTask> thirdTasks = new ThirdTasks().
                    getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
            thirdTask = thirdTasks.get(task);
        }
    }

    private void initializeViews(View rootView) {
        textViewT31 = rootView.findViewById(R.id.textViewT31);
        textViewT32 = rootView.findViewById(R.id.textViewT32);
        textViewT33 = rootView.findViewById(R.id.textViewT33);

        editText31 = rootView.findViewById(R.id.editText31);
        editText32 = rootView.findViewById(R.id.editText32);
        editText33 = rootView.findViewById(R.id.editText33);

        imageView3 = rootView.findViewById(R.id.imageView3);

        buttonEnd = rootView.findViewById(R.id.buttonEnd);
    }

    private void setInfo() {
        imageView3.setImageDrawable(getResources().
                getDrawable(thirdTask.getImgThirdTask()));
        textViewT31.setText(thirdTask.getTextViewT31());
        textViewT32.setText(thirdTask.getTextViewT32());
        textViewT33.setText(thirdTask.getTextViewT33());
    }

}

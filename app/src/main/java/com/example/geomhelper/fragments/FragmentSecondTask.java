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
import com.example.geomhelper.content.SecondTask;
import com.example.geomhelper.content.SecondTasks;
import com.example.geomhelper.content.Test;
import com.example.geomhelper.content.Tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Person.task;
import static com.example.geomhelper.fragments.FragmentResult.answer2;
import static com.example.geomhelper.fragments.FragmentTests.fab;

public class FragmentSecondTask extends Fragment {

    private TextView textViewTask2;
    private ImageView imageView2;
    private EditText editText2Task;
    private Button buttonEnter2;
    private SecondTask secondTask;
    static int fabTest = 0, fabTheme = 0, fabStage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second_task, container, false);

        initializeTests();
        initializeViews(rootView);
        setInfo();

        buttonEnter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer2 = editText2Task.getText().toString();
                FragmentThirdTask fragmentThirdTask = new FragmentThirdTask();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction().replace(
                        R.id.frame_tests, fragmentThirdTask).addToBackStack(null).commit();
            }
        });
        return rootView;
    }

    private void initializeTests() {
        List<Test> tests = new Tests().getCurrentTests();

        if (fab)
            secondTask = new SecondTasks().getTasks(tests.size() - 1 - fabTest, fabTheme).get(fabStage);
        else {
            ArrayList<SecondTask> secondTasks = new SecondTasks().
                    getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
            secondTask = secondTasks.get(task);
        }
    }

    private void initializeViews(View rootView) {
        textViewTask2 = rootView.findViewById(R.id.textViewTask2);

        imageView2 = rootView.findViewById(R.id.imageView2);

        editText2Task = rootView.findViewById(R.id.editText2Task);

        buttonEnter2 = rootView.findViewById(R.id.buttonEnter2);
    }

    private void setInfo() {
        textViewTask2.setText(secondTask.getTextViewTask2());
        imageView2.setImageDrawable(getResources()
                .getDrawable(secondTask.getImageView2()));
    }

}
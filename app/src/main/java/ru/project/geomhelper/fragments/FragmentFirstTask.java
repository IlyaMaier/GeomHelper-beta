package ru.project.geomhelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.project.geomhelper.Person;
import ru.project.geomhelper.R;
import ru.project.geomhelper.activities.MainActivity;
import ru.project.geomhelper.content.FirstTask;
import ru.project.geomhelper.content.FirstTasks;
import ru.project.geomhelper.content.Test;
import ru.project.geomhelper.content.Tests;
import ru.project.geomhelper.retrofit.TestJSON;
import ru.project.geomhelper.sqlite.DB;

import static ru.project.geomhelper.Person.task;
import static ru.project.geomhelper.fragments.FragmentResult.answer1;
import static ru.project.geomhelper.fragments.FragmentTests.fab;
import static ru.project.geomhelper.sqlite.OpenHelper.NUM_COLUMN_TESTS;

public class FragmentFirstTask extends Fragment {

    private TextView textViewTask1;
    private ImageView imageView1;
    private RadioButton radioButton, radioButton1, radioButton2;
    private Button buttonEnter1;
    private FirstTask firstTask;
    private boolean t = true;
    public static int fabTest = 0, fabTheme = 0, fabStage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_task, container, false);

        initializeTests();
        initializeViews(rootView);
        setInfo();

        buttonEnter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.isChecked())
                    answer1 = 0;
                else if (radioButton1.isChecked())
                    answer1 = 1;
                else if (radioButton2.isChecked())
                    answer1 = 2;

                FragmentSecondTask fragmentSecondTask = new FragmentSecondTask();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction()
                        .replace(R.id.frame_tests, fragmentSecondTask).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    void initializeTests() {
        List<Test> tests = new Tests().getCurrentTests();

        DB db = new DB(getContext());

        if (fab)
            firstTask = new FirstTasks().getTasks(tests.size() - 1 - fabTest, fabTheme).get(fabStage);
        else {
            TestJSON testJSON = new Gson().fromJson(
                    db.getString(NUM_COLUMN_TESTS), TestJSON.class);
            if (testJSON == null) {
                testJSON = new TestJSON();
                testJSON.setTest(Person.currentTest, Person.currentTestTheme, 0);
            } else {
                try {
                    task = testJSON.getTest(Person.currentTest, Person.currentTestTheme);
                } catch (Exception e) {
                    testJSON = new TestJSON();
                    testJSON.setTest(Person.currentTest, Person.currentTestTheme, 0);
                }
            }

            ArrayList<FirstTask> firstTasks = new FirstTasks().
                    getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
            if (firstTasks.size() == 0 || task == firstTasks.size()) {
                t = false;
                Toast.makeText(getContext(), R.string.tests_not_available,
                        Toast.LENGTH_SHORT).show();
                close();
            } else firstTask = firstTasks.get(task);
        }
    }

    void initializeViews(View rootView) {
        textViewTask1 = rootView.findViewById(R.id.textViewTask1);

        imageView1 = rootView.findViewById(R.id.imageView1);

        radioButton = rootView.findViewById(R.id.radioButton);
        radioButton1 = rootView.findViewById(R.id.radioButton1);
        radioButton2 = rootView.findViewById(R.id.radioButton2);

        buttonEnter1 = rootView.findViewById(R.id.buttonEnter1);
    }

    void setInfo() {
        if (t) {
            textViewTask1.setText(firstTask.getTextViewTask1());
            imageView1.setImageDrawable(getResources().
                    getDrawable(firstTask.getImageView1()));
            radioButton.setText(firstTask.getRadioButton());
            radioButton1.setText(firstTask.getRadioButton1());
            radioButton2.setText(firstTask.getRadioButton2());
        }
    }

    void close() {
        MainActivity.back = 3;
        Person.backTests = 3;
        FragmentTransaction fragmentTransaction =
                Objects.requireNonNull(getFragmentManager()).beginTransaction();
        FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
        fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
        fragmentTransaction.commit();
    }

}

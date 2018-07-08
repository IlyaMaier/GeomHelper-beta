package ru.project.geomhelper.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.project.geomhelper.Person;
import ru.project.geomhelper.R;
import ru.project.geomhelper.content.Achievements;
import ru.project.geomhelper.content.Course;
import ru.project.geomhelper.content.Courses;
import ru.project.geomhelper.fragments.FragmentAchievements;
import ru.project.geomhelper.retrofit.User;
import ru.project.geomhelper.retrofit.UserService;
import ru.project.geomhelper.sqlite.DB;

import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_ACHIEVEMENTS;
import static ru.project.geomhelper.sqlite.OpenHelper.NUM_COLUMN_ACHIEVEMENTS;

public class AddCourseActivity extends AppCompatActivity {

    private List<Course> coursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_activity);

        coursesList = new Courses().getCurrentCourses();

        if (Person.courses.size() == coursesList.size()) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Toast.makeText(getApplicationContext(), R.string.no_courses, Toast.LENGTH_LONG).show();
            finish();
        } else {
            ArrayList<Course> courses = new ArrayList<>();
            Course course;
            for (int i = 0; i < coursesList.size(); i++) {
                course = coursesList.get(i);
                if (!Person.c.contains(i + "")) courses.add(course);
            }

            RecyclerView recyclerView = findViewById(R.id.recycler_add_course);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            RVAdapter rvAdapter = new RVAdapter(getApplicationContext(), courses);
            recyclerView.setAdapter(rvAdapter);
        }
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RecyclerViewCoursesHolder> {

        ArrayList<Course> items;
        Context context;
        DB db;

        RVAdapter(Context context, ArrayList<Course> items) {
            this.items = items;
            this.context = context;
            db = new DB(context);
        }

        @NonNull
        @Override
        public RVAdapter.RecyclerViewCoursesHolder onCreateViewHolder(
                @NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_courses_card, parent, false);
            return new RVAdapter.RecyclerViewCoursesHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RVAdapter.RecyclerViewCoursesHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class RecyclerViewCoursesHolder extends RecyclerView.ViewHolder {

            private CardView cardView;
            private TextView title;
            private ImageView image;
            private Course course;

            RecyclerViewCoursesHolder(final View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                image = itemView.findViewById(R.id.image_content_courses);
                cardView = itemView.findViewById(R.id.card_content_courses);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Person.courses.contains(course))
                            Person.courses.add(0, course);
                        Person.c += String.valueOf(coursesList.indexOf(course));

                        Achievements achievements = new Gson().fromJson(
                                db.getString(NUM_COLUMN_ACHIEVEMENTS), Achievements.class);
                        if (achievements == null) achievements = new Achievements();
                        achievements.setCourses(achievements.getCourses() + 1);
                        db.putString(COLUMN_ACHIEVEMENTS, new Gson().toJson(
                                achievements, Achievements.class));

                        if (achievements.getCourses() == FragmentAchievements.courses
                                && !achievements.isCoursesB()) {
                            achievements.setCoursesB(true);
                            Person.experience += FragmentAchievements.coursesE;

                            NotificationManager notificationManager = (NotificationManager)
                                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "0")
                                    .setSmallIcon(R.drawable.ic_menu_leaderboard)
                                    .setColor(getResources().getColor(R.color.leaderboard))
                                    .setContentTitle(getString(R.string.app_name))
                                    .setContentText(getString(R.string.achievement_done) +
                                            FragmentAchievements.coursesE + getString(R.string.xp))
                                    .setAutoCancel(true)
                                    .setWhen(System.currentTimeMillis())
                                    .setShowWhen(true);
                            Notification notification = builder.build();
                            Objects.requireNonNull(notificationManager).notify(0, notification);
                        }

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(User.URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        UserService userService = retrofit.create(UserService.class);
                        userService.updateUser(Person.uId, getString(R.string.param_courses), Person.c)
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                        if (Objects.requireNonNull(response.body()).equals("0"))
                                            Toast.makeText(context, R.string.can_not_send_data,
                                                    Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        Toast.makeText(context, R.string.can_not_send_data,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        userService.updateUser(Person.uId, "achievements",
                                db.getString(NUM_COLUMN_ACHIEVEMENTS))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                        if (Objects.requireNonNull(response.body()).equals("0"))
                                            Toast.makeText(context, R.string.can_not_send_data,
                                                    Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        Toast.makeText(context, R.string.can_not_send_data,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        userService.updateUser(Person.uId, "experience", String.valueOf(Person.experience))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                        if (Objects.requireNonNull(response.body()).equals("0"))
                                            Toast.makeText(context, R.string.can_not_send_data,
                                                    Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        Toast.makeText(context, R.string.can_not_send_data,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

            }

            void bind(Course course) {
                this.course = course;
                image.setImageBitmap(BitmapFactory.decodeResource(itemView.getResources(), course.getBackground()));
                title.setText(course.getCourseName());
            }

        }

    }

}

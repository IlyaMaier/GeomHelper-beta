package ru.project.geomhelper.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.project.geomhelper.Person;
import ru.project.geomhelper.R;
import ru.project.geomhelper.activities.MainActivity;
import ru.project.geomhelper.content.Achievements;
import ru.project.geomhelper.content.Levels;
import ru.project.geomhelper.resources.CircleImageView;
import ru.project.geomhelper.resources.RVLeaderboardAdapter;
import ru.project.geomhelper.retrofit.User;
import ru.project.geomhelper.retrofit.UserService;
import ru.project.geomhelper.sqlite.DB;

import static android.app.Activity.RESULT_OK;
import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_ACHIEVEMENTS;
import static ru.project.geomhelper.sqlite.OpenHelper.NUM_COLUMN_ACHIEVEMENTS;
import static ru.project.geomhelper.sqlite.OpenHelper.NUM_DESC;

public class FragmentProfile extends Fragment {

    private TextView textName;
    private CircleImageView circleImageView;
    private Bitmap bitmap;
    private ScrollView scrollView;
    private Context context;
    public static volatile boolean d = false, a = false, social = false;
    public static String personPhotoUrl = "";

    public FragmentProfile() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getContext();

        DB db = new DB(context);

        Achievements achievs = new Gson().fromJson(
                db.getString(NUM_COLUMN_ACHIEVEMENTS), Achievements.class);
        if (achievs == null) achievs = new Achievements();

        if (Person.experience >= FragmentAchievements.xp
                && !achievs.isXp()) {
            Person.experience += FragmentAchievements.xpE;
            achievs.setXp(true);
            db.putString(COLUMN_ACHIEVEMENTS, new Gson().toJson(achievs, Achievements.class));
            NotificationManager notificationManager = (NotificationManager)
                    Objects.requireNonNull(getContext()).getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "0")
                    .setSmallIcon(R.drawable.ic_menu_leaderboard)
                    .setColor(getResources().getColor(R.color.leaderboard))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.achievement_done) +
                            FragmentAchievements.xpE + "xp")
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true);
            Notification notification = builder.build();
            Objects.requireNonNull(notificationManager).notify(0, notification);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(User.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            UserService userService = retrofit.create(UserService.class);
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
                            Toast.makeText(getContext(), R.string.can_not_send_data,
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
        }

        textName = rootView.findViewById(R.id.textName);
        TextView textLevelName = rootView.findViewById(R.id.textLevelName);
        TextView textExperience = rootView.findViewById(R.id.textExperince);
        circleImageView = rootView.findViewById(R.id.imageProfile);
        Button theorems = rootView.findViewById(R.id.teorems);
        Button definitions = rootView.findViewById(R.id.definitions);
        Button achievements = rootView.findViewById(R.id.achievements);

        if (social) {
            Glide.with(getContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(circleImageView);
            new Async().execute();
        }

        Person.currentLevel = new Levels().getLevel(Person.experience);
        Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
        textName.setText(Person.name);
        textLevelName.setText(Person.currentLevel);
        textExperience.setText((Person.experience + "/" + Person.currentLevelExperience));

        try {
            try {
                bitmap = BitmapFactory.decodeFile(
                        context.getFilesDir() +
                                "/profileImage.png");

                circleImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 500);
            }
        });

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(context);
                et.setText(Person.name);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(et)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = et.getText().toString();
                                if (name.contains("\n"))
                                    name = name.replaceAll("\n", "");
                                if (name.isEmpty()) {
                                    dialog.cancel();
                                } else {
                                    if (name.length() > 20) name = name.substring(0, 20);
                                    Person.name = name;

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(User.URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .build();
                                    UserService userService = retrofit.create(UserService.class);
                                    userService.updateUser(Person.uId, "name", Person.name)
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
                                    textName.setText(Person.name);
                                }
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
            }
        });
        scrollView = rootView.findViewById(R.id.scroll_profile);

        definitions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.back = 6;
                scrollView.setVisibility(View.INVISIBLE);
                scrollView.setClickable(false);
                FragmentTransaction fragmentTransaction =
                        Objects.requireNonNull(getFragmentManager()).beginTransaction();
                FragmentDefinitions fragmentDefinitions = new FragmentDefinitions();
                fragmentTransaction.replace(R.id.frame_profile, fragmentDefinitions);
                fragmentTransaction.commit();
            }
        });

        theorems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.back = 6;
                scrollView.setVisibility(View.INVISIBLE);
                scrollView.setClickable(false);
                FragmentTransaction fragmentTransaction =
                        Objects.requireNonNull(getFragmentManager()).beginTransaction();
                FragmentTheorems fragmentTheorems = new FragmentTheorems();
                fragmentTransaction.replace(R.id.frame_profile, fragmentTheorems);
                fragmentTransaction.commit();
            }
        });

        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.back = 6;
                scrollView.setVisibility(View.INVISIBLE);
                scrollView.setClickable(false);
                FragmentTransaction fragmentTransaction =
                        Objects.requireNonNull(getFragmentManager()).beginTransaction();
                FragmentAchievements fragmentAchievements = new FragmentAchievements();
                fragmentTransaction.replace(R.id.frame_profile, fragmentAchievements);
                fragmentTransaction.commit();
            }
        });

        final List<User> friends = new ArrayList<>();

        final RVLeaderboardAdapter rvLeaderboardAdapter = new RVLeaderboardAdapter(getContext(), friends, false);

        final RecyclerView recyclerView = rootView.findViewById(R.id.rv_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rvLeaderboardAdapter);

        int desc = db.getInt(NUM_DESC);
        if (desc < 10)
            desc = 10;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(User.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        UserService userService = retrofit.create(UserService.class);
        userService.getLeaders(desc).enqueue(new Callback<String>() {
            String result;

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                result = response.body();
                result = Objects.requireNonNull(result).replace("[", "");
                result = result.replace("]", "");
                String[] split = result.split(",");
                Gson gson = new Gson();
                StringBuilder q = new StringBuilder();
                int a = 0;
                for (String aSplit : split) {
                    if (aSplit.contains("}")) q.append(aSplit);
                    else q.append(aSplit).append(",");
                    a++;
                    if (aSplit.contains("}")) {
                        friends.add(gson.fromJson(q.toString(), User.class));
                        a = 0;
                        q = new StringBuilder();
                    }
                }

                rvLeaderboardAdapter.setData(friends);
                rvLeaderboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        textName.setText(Person.name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 500:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        if (selectedImage != null) {
                            imageStream = Objects.requireNonNull(getActivity())
                                    .getContentResolver().openInputStream(selectedImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    circleImageView.setImageBitmap(yourSelectedImage);
                    try {
                        File file = new File(context.getFilesDir(), "/profileImage.png");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } finally {
                            if (fos != null) fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    Uri file1 = Uri.fromFile(new File(Objects.requireNonNull(getContext()).getFilesDir(),
                            "profileImage.png"));
                    StorageReference profileRef = mStorageRef.child(Person.uId);
                    profileRef.putFile(file1)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getContext(),
                                            R.string.can_not_load_image,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Async extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap1;
            try {
                bitmap1 = Glide
                        .with(context)
                        .load(personPhotoUrl)
                        .asBitmap()
                        .into(100, 100)
                        .get();
                File file = new File(context.getFilesDir(), "/profileImage.png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (social) {
                    try {
                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                        Uri file1 = Uri.fromFile(new File(Objects.requireNonNull(getContext()).getFilesDir(),
                                "profileImage.png"));
                        StorageReference profileRef = mStorageRef.child(Person.uId);
                        profileRef.putFile(file1)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        try {
                                            Toast.makeText(getContext(),
                                                    R.string.can_not_load_image,
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        d = false;
                                    }
                                });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    d = true;
                    new Async1().execute();
                }
                social = false;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Async1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (d) {
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            try {
                try {
                    bitmap = BitmapFactory.decodeFile(
                            context.getFilesDir() +
                                    "/profileImage.png");

                    circleImageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}

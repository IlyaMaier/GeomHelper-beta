package com.example.geomhelper.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.activities.LoginActivity;
import com.example.geomhelper.content.Achievements;
import com.example.geomhelper.retrofit.User;
import com.example.geomhelper.retrofit.UserService;
import com.example.geomhelper.sqlite.DB;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_ACHIEVEMENTS;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_C;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_DAY_NIGHT;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_DESC;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_EXPERIENCE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_FACEBOOK;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_GOOGLE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_IMAGE_LEADERS;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_LEADERBOARDPLACE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_LEVEL;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_LEVEL_EXPERIENCE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_NAME;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_SETTINGS;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_TESTS;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_UID;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_VK;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_ACHIEVEMENTS;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_FACEBOOK;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_GOOGLE;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_COLUMN_VK;
import static com.example.geomhelper.sqlite.OpenHelper.NUM_DESC;

public class FragmentSettings extends PreferenceFragmentCompat
        implements GoogleApiClient.OnConnectionFailedListener {

    private EditTextPreference editTextPreferenceName, descPreference;
    private Activity mCurrentActivity;
    private AlertDialog.Builder builder;
    private GoogleApiClient mGoogleApiClient;
    private DB db;
    private int desc;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        db = new DB(getContext());

        mCurrentActivity = getActivity();
        setPreferencesFromResource(R.xml.preferences, rootKey);

        editTextPreferenceName = (EditTextPreference) findPreference("name");
        editTextPreferenceName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editTextPreferenceName.setText(Person.name);
                return false;
            }
        });

        editTextPreferenceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String name = newValue.toString();
                if (name.contains("\n"))
                    name = name.replaceAll("\n", "");
                if (newValue.toString().isEmpty()) {
                    return false;
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
                                        Toast.makeText(getContext(), R.string.can_not_send_name,
                                                Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), R.string.can_not_send_name,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    return true;
                }
            }
        });

        desc = db.getInt(NUM_DESC);
        if (desc < 10)
            desc = 10;

        editTextPreferenceName = (EditTextPreference) findPreference("desc");
        editTextPreferenceName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editTextPreferenceName.setText(String.valueOf(desc));
                return false;
            }
        });

        editTextPreferenceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int v;
                try {
                    v = Integer.parseInt(newValue.toString());
                } catch (Exception e) {
                    v = 10;
                }
                if (v < 10)
                    desc = 10;
                else desc = v;
                db.putInt(COLUMN_DESC, desc);
                return true;
            }
        });

        ListPreference listPreference = (ListPreference) findPreference("pref_day_night");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()

        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                saveAll();
                if (newValue.equals("Включен")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    db.putString(COLUMN_DAY_NIGHT, getString(R.string.on));
                } else if (newValue.equals(getString(R.string.off))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    db.putString(COLUMN_DAY_NIGHT, getString(R.string.off));
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    db.putString(COLUMN_DAY_NIGHT, getString(R.string.auto));
                }
                db.putInt(COLUMN_SETTINGS, 1);
                return true;
            }
        });

        Preference share = findPreference("share");
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Achievements achievements = new Gson().fromJson(
                        db.getString(NUM_COLUMN_ACHIEVEMENTS), Achievements.class);
                if (achievements == null) achievements = new Achievements();
                achievements.setShared(achievements.getShared() + 1);

                if (!achievements.isSharedB()) {
                    achievements.setSharedB(true);
                    Person.experience += FragmentAchievements.sharedE;
                    NotificationManager notificationManager = (NotificationManager)
                            Objects.requireNonNull(getContext()).getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "0")
                            .setSmallIcon(R.drawable.ic_menu_leaderboard)
                            .setColor(getResources().getColor(R.color.leaderboard))
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(getString(R.string.achievement_done) +
                                    FragmentAchievements.sharedE + "xp")
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setShowWhen(true);
                    Notification notification = builder.build();
                    Objects.requireNonNull(notificationManager).notify(0, notification);
                }
                db.putString(COLUMN_ACHIEVEMENTS, new Gson().toJson(
                        achievements, Achievements.class));

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
                                if (response.body() == null || response.body().equals("0"))
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
                                if (response.body() == null || response.body().equals("0"))
                                    Toast.makeText(getContext(), R.string.can_not_send_data,
                                            Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), R.string.can_not_send_data,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.download_app) +
                                "https://yadi.sk/d/ub5kRUYy3SSHWC");
                i.setType("text/plain");
                startActivity(Intent.createChooser(i, getString(R.string.share)));
                return false;
            }
        });

        Preference news = findPreference("news");
        news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Что нового");
                builder.setMessage(getString(R.string.whats_new));
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        Preference aboutAuthors = findPreference("about_authors");
        aboutAuthors.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.about_authors);
                builder.setMessage(R.string.app_done);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.about);
                builder.setMessage(R.string.about_app);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        Preference acc = findPreference("acc");
        acc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.are_you_sure);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Person.name = "";
                        Person.uId = "";
                        Person.courses.clear();
                        Person.experience = 0;
                        Person.leaderBoardPlace = 0;
                        Person.c = "";
                        Person.currentTest = 0;
                        Person.currentTestTheme = 0;
                        saveAll();

                        Objects.requireNonNull(getActivity()).getSharedPreferences(
                                Person.APP_PREFERENCES, Context.MODE_PRIVATE)
                                .edit().putLong("id", -1).apply();

                        Achievements achievements = new Achievements();
                        db.putString(COLUMN_ACHIEVEMENTS, new Gson().toJson(
                                achievements, Achievements.class));

                        db.putString(COLUMN_TESTS, "");
                        db.putString(COLUMN_DAY_NIGHT, "2");

                        if (db.getInt(NUM_COLUMN_FACEBOOK) == 1) {
                            LoginManager.getInstance().logOut();
                            db.putInt(COLUMN_FACEBOOK, 0);
                        }
                        if (db.getInt(NUM_COLUMN_GOOGLE) == 1) {
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            db.putInt(COLUMN_GOOGLE, 0);
                        }
                        db.putInt(COLUMN_VK, 0);
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return false;
            }
        });


        Preference email = findPreference("email");
        email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (db.getInt(NUM_COLUMN_FACEBOOK) == 0
                        || db.getInt(NUM_COLUMN_GOOGLE) == 0
                        || db.getInt(NUM_COLUMN_VK) == 0) {
                    Toast.makeText(getContext(),
                            R.string.can_not_change_email,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                builder = new AlertDialog.Builder(getContext());
                final LayoutInflater factory = LayoutInflater.from(getContext());
                final View textEntryView = factory.inflate(R.layout.alert_email, null);

                final EditText input1 = textEntryView.findViewById(R.id.et_email1);
                final EditText input2 = textEntryView.findViewById(R.id.et_email2);

                builder.setTitle(R.string.change_email);
                builder.setView(textEntryView);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int arg1) {
                        if (input2.getText() == null || input2.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), R.string.void_email,
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(User.URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        UserService userService = retrofit.create(UserService.class);
                        userService.changeEmail(Person.uId, User.md5(input1.getText().toString())
                                , input2.getText().toString())
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call,
                                                           @NonNull Response<String> response) {
                                        String r = response.body();
                                        if (Objects.requireNonNull(r).equals("0"))
                                            Toast.makeText(getContext(), R.string.error_on_server,
                                                    Toast.LENGTH_SHORT).show();
                                        else if (r.equals("2")) {
                                            Toast.makeText(getContext(), R.string.wrong_password,
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (r.equals("3")) {
                                            Toast.makeText(getContext(), R.string.this_email_is_busy,
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (r.equals("1")) {
                                            Toast.makeText(getContext(), R.string.changed_email,
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        dialog.cancel();
                                    }
                                });
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.show();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textEntryView.getLayoutParams();
                params.setMargins(24, 24, 24, 24);

                return false;
            }
        });

        Preference password = findPreference("password");
        password.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

        {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (db.getInt(NUM_COLUMN_FACEBOOK) == 0
                        || db.getInt(NUM_COLUMN_GOOGLE) == 0
                        || db.getInt(NUM_COLUMN_VK) == 0) {
                    Toast.makeText(getContext(),
                            R.string.can_not_change_password,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                builder = new AlertDialog.Builder(getContext());
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View textEntryView = factory.inflate(R.layout.alert_password, null);

                final EditText input1 = textEntryView.findViewById(R.id.et_password1);
                final EditText input2 = textEntryView.findViewById(R.id.et_password2);
                final EditText input3 = textEntryView.findViewById(R.id.et_password3);

                builder.setTitle(R.string.change_password);
                builder.setView(textEntryView);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int arg1) {
                        if (input1.getText() == null || input1.getText().toString().isEmpty()
                                || input2.getText() == null || input2.getText().toString().isEmpty()
                                || input3.getText() == null || input3.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), R.string.void_pass,
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        if (!input2.getText().toString().equals(input3.getText().toString())) {
                            Toast.makeText(getContext(), R.string.passwords_not_correct,
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(User.URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        UserService userService = retrofit.create(UserService.class);
                        userService.changePassword(Person.uId, User.md5(input1.getText().toString())
                                , User.md5(input2.getText().toString()))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call,
                                                           @NonNull Response<String> response) {
                                        String r = response.body();
                                        if (Objects.requireNonNull(r).equals("0"))
                                            Toast.makeText(getContext(), R.string.error_on_server,
                                                    Toast.LENGTH_SHORT).show();
                                        else if (r.equals("2")) {
                                            Toast.makeText(getContext(), R.string.wrong_password,
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (r.equals("1")) {
                                            Toast.makeText(getContext(), R.string.changed_pass,
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        dialog.cancel();
                                    }
                                });
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.show();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textEntryView.getLayoutParams();
                params.setMargins(24, 24, 24, 24);

                return false;
            }
        });

        Preference imagePreference = findPreference("imageLeaders");
        imagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()

        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.equals(true)) db.putInt(COLUMN_IMAGE_LEADERS, 1);
                else if (newValue.equals(false)) db.putInt(COLUMN_IMAGE_LEADERS, 0);
                return true;
            }
        });

    }


    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
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

}

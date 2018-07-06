package com.example.geomhelper.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.content.Achievements;
import com.example.geomhelper.content.Levels;
import com.example.geomhelper.fragments.FragmentProfile;
import com.example.geomhelper.retrofit.User;
import com.example.geomhelper.retrofit.UserService;
import com.example.geomhelper.sqlite.DB;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_FACEBOOK;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_GOOGLE;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_UID;
import static com.example.geomhelper.sqlite.OpenHelper.COLUMN_VK;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private final int RC_SIGN_IN = 9007;
    private EditText mEmail;
    private EditText mPassword;
    private GoogleApiClient mGoogleApiClient;
    private String tests, achievements;
    private DB db;

    private UserService userService;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        //action bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }

        setContentView(R.layout.activity_login);

        //views
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        TextView textView = findViewById(R.id.not_register);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });

        //buttons
        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.vk).setOnClickListener(this);
        findViewById(R.id.facebook).setOnClickListener(this);
        findViewById(R.id.google).setOnClickListener(this);

        //facebook
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getMeInfo();
            }

            @Override
            public void onCancel() {
                progressDialog.cancel();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),
                        R.string.error_signin_facebook,
                        Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

        //google+
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(User.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        VKSdk.initialize(getApplicationContext());

        db = new DB(getApplicationContext());
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.fill_in));
            valid = false;
        }

        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.fill_in));
            valid = false;
        }

        if (password.length() < 6) {
            mPassword.setError(getString(R.string.too_short_password));
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.sign_in:
                signIn();
                break;
            case R.id.vk:
                signInWithVK();
                break;
            case R.id.facebook:
                signInWithFacebook();
                break;
            case R.id.google:
                signInWithGoogle();
                break;
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.show();
    }

    void signIn() {
        if (validateForm()) {
            userService.login(mEmail.getText().toString(),
                    User.md5(mPassword.getText().toString()))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            switch (Objects.requireNonNull(response.body())) {
                                case "0":
                                    Toast.makeText(getApplicationContext(), R.string.error,
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    break;
                                case "2":
                                    Toast.makeText(getApplicationContext(),
                                            R.string.person_not_found,
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    break;
                                case "3":
                                    Toast.makeText(getApplicationContext(), R.string.wrong_password,
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    break;
                                default:
                                    User user = new Gson().fromJson(response.body(), User.class);
                                    Person.uId = String.valueOf(user.getId());
                                    Person.name = user.getName();
                                    Person.experience = user.getExperience();
                                    Person.currentLevel = new Levels().getLevel(Person.experience);
                                    Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                    Person.c = user.getCourses();
                                    tests = user.getTests();
                                    achievements = user.getAchievements();

                                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                                    try {
                                        StorageReference profileRef = mStorageRef.child(Person.uId);
                                        File file = new File(getFilesDir(),
                                                "profileImage.png");
                                        profileRef.getFile(file).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(getApplicationContext(),
                                                        getString(R.string.can_not_send_data),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    saveAndFinish();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
        }

    }

    void saveAndFinish() {
        if (achievements == null)
            achievements = new Gson().toJson(new Achievements(), Achievements.class);
        Person.id = db.signIn(Person.uId, Person.name, 1, Person.c, tests, achievements,
                Person.experience, Person.currentLevel, Person.currentLevelExperience);
        getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putLong("id", Person.id).apply();

        db.putString(COLUMN_UID, Person.uId);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

        CharSequence text;
        if (Person.name != null)
            text = getString(R.string.welcome1) + Person.name + "!";
        else text = getString(R.string.welcome2);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }

    void signInWithFacebook() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginManager.logInWithReadPermissions(
                this,
                Arrays.asList("public_profile", "email"));
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {
                final String email = res.email;
                if (email == null) {
                    Toast.makeText(LoginActivity.this,
                            R.string.error_signin_vk,
                            Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                } else {
                    VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, res.userId))
                            .executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    try {
                                        final String name = response.json.getJSONArray("response").getJSONObject(0)
                                                .getString("first_name") + " " +
                                                response.json.getJSONArray("response").getJSONObject(0)
                                                        .getString("last_name");
                                        userService.loginWithSocial(email, User.md5(getString(R.string.soc)), name)
                                                .enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<String> call,
                                                                           @NonNull Response<String> response) {
                                                        if (Objects.requireNonNull(response.body()).equals("0")) {
                                                            Toast.makeText(LoginActivity.this,
                                                                    R.string.error_signin_vk,
                                                                    Toast.LENGTH_SHORT).show();
                                                            progressDialog.cancel();
                                                        } else {
                                                            if (Objects.requireNonNull(response.body()).length() > 10) {
                                                                User user = new Gson().fromJson(response.body(), User.class);
                                                                Person.uId = String.valueOf(user.getId());
                                                                Person.name = user.getName();
                                                                Person.experience = user.getExperience();
                                                                Person.currentLevel = new Levels().getLevel(Person.experience);
                                                                Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                                                Person.c = user.getCourses();
                                                                tests = user.getTests();
                                                                achievements = user.getAchievements();
                                                            } else {
                                                                Person.name = name;
                                                                Person.uId = response.body();
                                                            }
                                                            VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo"));
                                                            request.executeWithListener(new VKRequest.VKRequestListener() {
                                                                @Override
                                                                public void onComplete(VKResponse response) {
                                                                    try {
                                                                        FragmentProfile.personPhotoUrl = response.json
                                                                                .getJSONArray("response").getJSONObject(0)
                                                                                .getString("photo");
                                                                    } catch (JSONException e) {
                                                                        Toast.makeText(LoginActivity.this,
                                                                                R.string.can_not_load_image,
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                                                    Toast.makeText(LoginActivity.this,
                                                                            R.string.can_not_load_image,
                                                                            Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onError(VKError error) {
                                                                    Toast.makeText(LoginActivity.this,
                                                                            R.string.can_not_load_image,
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            FragmentProfile.social = true;
                                                            saveAndFinish();
                                                            db.putInt(COLUMN_VK, 1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                        Toast.makeText(LoginActivity.this,
                                                                R.string.error_signin_vk,
                                                                Toast.LENGTH_SHORT).show();
                                                        progressDialog.cancel();
                                                    }
                                                });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this,
                                                R.string.error_signin_vk,
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                }

                                @Override
                                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                    Toast.makeText(getApplicationContext(),
                                            R.string.error_signin_vk, Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }

                                @Override
                                public void onError(VKError error) {
                                    Toast.makeText(getApplicationContext(),
                                            R.string.error_signin_vk, Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });
                }
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),
                        R.string.error_signin_vk, Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        })) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    public void getMeInfo() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        final GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "me",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object;
                        try {
                            if (response.getError() == null) {
                                object = new JSONObject(response.getRawResponse());
                                final String name = object.getString("name");
                                String email = object.getString("email");
                                final String photo = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                userService.loginWithSocial(email, User.md5(getString(R.string.soc)), name)
                                        .enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(@NonNull Call<String> call,
                                                                   @NonNull Response<String> response) {
                                                if (Objects.requireNonNull(response.body()).equals("0")) {
                                                    Toast.makeText(LoginActivity.this,
                                                            R.string.error_signin_facebook,
                                                            Toast.LENGTH_SHORT).show();
                                                    progressDialog.cancel();
                                                } else {
                                                    if (Objects.requireNonNull(response.body()).length() > 10) {
                                                        User user = new Gson().fromJson(response.body(), User.class);
                                                        Person.uId = String.valueOf(user.getId());
                                                        Person.name = user.getName();
                                                        Person.experience = user.getExperience();
                                                        Person.currentLevel = new Levels().getLevel(Person.experience);
                                                        Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                                        Person.c = user.getCourses();
                                                        tests = user.getTests();
                                                        achievements = user.getAchievements();
                                                    } else {
                                                        Person.name = name;
                                                        Person.uId = response.body();
                                                    }
                                                    FragmentProfile.personPhotoUrl = photo;
                                                    FragmentProfile.social = true;
                                                    saveAndFinish();
                                                    db.putInt(COLUMN_FACEBOOK, 1);
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                Toast.makeText(LoginActivity.this,
                                                        R.string.error_signin_facebook,
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }
                                        });
                            } else
                                Toast.makeText(LoginActivity.this, R.string.error_signin_facebook,
                                        Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,
                                    R.string.error_signin_facebook,
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleApiClient.connect();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            Person.name = Objects.requireNonNull(acct).getDisplayName();
            FragmentProfile.personPhotoUrl = Objects.requireNonNull(acct.getPhotoUrl()).toString();
            FragmentProfile.social = true;

            userService.loginWithSocial(acct.getEmail(), User.md5(getString(R.string.soc)), Person.name)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call,
                                               @NonNull Response<String> response) {
                            if (Objects.requireNonNull(response.body()).equals("0")) {
                                Toast.makeText(LoginActivity.this,
                                        R.string.error_signin_google,
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            } else {
                                if (Objects.requireNonNull(response.body()).length() > 10) {
                                    User user = new Gson().fromJson(response.body(), User.class);
                                    Person.uId = String.valueOf(user.getId());
                                    Person.name = user.getName();
                                    Person.experience = user.getExperience();
                                    Person.currentLevel = new Levels().getLevel(Person.experience);
                                    Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                    Person.c = user.getCourses();
                                    tests = user.getTests();
                                    achievements = user.getAchievements();
                                } else {
                                    Person.uId = response.body();
                                }
                                db.putInt(COLUMN_GOOGLE, 1);
                                saveAndFinish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(LoginActivity.this,
                                    R.string.error_signin_google,
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this,
                    R.string.error_signin_google,
                    Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
    }

    void signInWithVK() {
        VKSdk.login(this, "email");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
    }

}

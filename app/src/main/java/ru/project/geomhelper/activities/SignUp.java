package ru.project.geomhelper.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.project.geomhelper.Person;
import ru.project.geomhelper.R;
import ru.project.geomhelper.resources.CircleImageView;
import ru.project.geomhelper.retrofit.User;
import ru.project.geomhelper.retrofit.UserService;
import ru.project.geomhelper.sqlite.DB;

import static ru.project.geomhelper.sqlite.OpenHelper.COLUMN_UID;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirm;
    private EditText mName;
    private CircleImageView circleImageView;
    private ProgressDialog progressDialog;
    private boolean a = false;

    protected void onCreate(Bundle savedInstanceState) {
        //action bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }

        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(SignUp.this);

        //views
        mEmail = findViewById(R.id.email_sign_up);
        mPassword = findViewById(R.id.password_sign_up);
        mConfirm = findViewById(R.id.confirm_sign_up);
        mName = findViewById(R.id.person_name);
        circleImageView = findViewById(R.id.imageProfileSignUp);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SignUp.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 6);
            }
        });

        findViewById(R.id.sign_up).setOnClickListener(this);

    }

    private boolean validateForm() {
        boolean valid = true;

        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.fill_in));
            valid = false;
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.fill_in));
            valid = false;
        }

        String password = mPassword.getText().toString();

        if (password.length() < 6) {
            mPassword.setError(getString(R.string.too_short_password));
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.fill_in));
            valid = false;
        }

        if (!password.equals(mConfirm.getText().toString())) {
            mConfirm.setError(getString(R.string.passwords_incorrect));
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)
                getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.sign_up) {
            if (!validateForm()) return;

            progressDialog.setTitle(getString(R.string.loading));
            progressDialog.show();

            if (!a) {
                try {
                    File file = new File(getFilesDir(), "/profileImage.png");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        BitmapFactory.decodeResource(
                                getResources(), R.drawable.back_login).compress(
                                Bitmap.CompressFormat.PNG, 100, fos);
                        a = true;
                    } finally {
                        if (fos != null) fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(User.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            UserService userService = retrofit.create(UserService.class);

            userService.createUser(mEmail.getText().toString(),
                    User.md5(mPassword.getText().toString()),
                    mName.getText().toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            switch (Objects.requireNonNull(response.body())) {
                                case "0":
                                    Toast.makeText(SignUp.this, R.string.error,
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case "2":
                                    Toast.makeText(SignUp.this,
                                            R.string.email_is_busy,
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Person.uId = response.body();
                                    Person.name = mName.getText().toString();

                                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                                    Uri file1 = Uri.fromFile(new File(Objects.requireNonNull(getApplicationContext()).getFilesDir(),
                                            "profileImage.png"));
                                    StorageReference profileRef = mStorageRef.child(Person.uId);
                                    profileRef.putFile(file1)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(getApplicationContext(),
                                                            getString(R.string.can_not_load_image),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    DB db = new DB(getApplicationContext());
                                    db.putString(COLUMN_UID, Person.uId);
                                    Person.id = db.signUp(Person.uId, Person.name, 1);
                                    getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE)
                                            .edit().putLong("id", Person.id).apply();

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    CharSequence text = getString(R.string.welcome1) + mName.getText() + "!";
                                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    startActivity(i);
                                    finish();
                                    break;
                            }
                            progressDialog.cancel();
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(getApplicationContext(), R.string.error,
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 6) {
            Bitmap yourSelectedImage;
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                if (selectedImage != null) {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            circleImageView.setImageBitmap(yourSelectedImage);
            try {
                File file = new File(getFilesDir(), "/profileImage.png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    yourSelectedImage.compress(
                            Bitmap.CompressFormat.PNG, 100, fos);
                    a = true;
                } finally {
                    if (fos != null) fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


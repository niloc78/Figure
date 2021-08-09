package com.example.figure;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;


import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;



public class LoginActivity extends AppCompatActivity {
    private static final String appId = "figure-mdlbd";
    ViewGroup sceneRoot;
    Transition set;
    Scene startScene;
    Scene loginScene;
    Scene resetScene;
    Scene registerScene;
    private StartScene currScene;
    private App app;
    private boolean loggedIn = false;
    ComponentName callingActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        if(isAlreadyLoggedIn()) {
            loggedIn = true;
            startMainLoggedIn();
        }

        setContentView(R.layout.user_start);
        initScenes();
        initStartButtons();
        currScene = StartScene.START;

        app = new App(new AppConfiguration.Builder(appId).build());

        callingActivity = getCallingActivity();
        //app.currentUser().logOut();


        //app.currentUser().logOut();
//        app.currentUser().logOutAsync(r -> {
//            if (r.isSuccess()) {
//                Log.d("LOGOUT", "SUCCESS");
//            } else {
//                Log.d("LOGOUT", "FAIL");
//            }
//        });

//        if (app.currentUser() != null && app.currentUser().isLoggedIn()) {
//            //logged in
//            //refresh data and pass to main
//            //set profile
//            startMain();
//        }

//        Log.d("CURR USER", "LOGGED IN: " + app.currentUser().isLoggedIn());
//        Log.d("CURR USER", "ID: " + app.currentUser().getId());
//        Log.d("CURR USER", "ACCESS TOKEN: " + app.currentUser().getAccessToken());
//        Log.d("CURR USER", "CUSTOM DATA: " + app.currentUser().getCustomData().toJson());
//        Log.d("CURR USER", "REFRESH TOKEN: " + app.currentUser().getRefreshToken());
//        app.currentUser().logOutAsync(it -> {
//            if (it.isSuccess()) {
//                Log.d("LOGOUT", "SUCCESS");
//            } else {
//                Log.d("LOGOUT", "FAILED: " + it.getError().toString());
//            }
//        });
//        Log.d("CURR USER", "LOGGED IN: " + app.currentUser().isLoggedIn());
//        Log.d("CURR USER", "ID: " + app.currentUser().getId());
//        Log.d("CURR USER", "ACCESS TOKEN: " + app.currentUser().getAccessToken());
//        Log.d("CURR USER", "CUSTOM DATA: " + app.currentUser().getCustomData().toJson());
//        Log.d("CURR USER", "REFRESH TOKEN: " + app.currentUser().getRefreshToken());



    }
    boolean isAlreadyLoggedIn() {
        App app = new App(new AppConfiguration.Builder(appId).build());
        return app.currentUser() != null && app.currentUser().isLoggedIn();
    }

    void startMainLoggedIn() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("logged_in", loggedIn);
        finish();
        startActivity(i);
    }

    boolean isForResult() {
        return callingActivity != null;
    }

    public void initResetButtons() {
        findViewById(R.id.send_reset_link_button).setOnClickListener(v -> {
            String email = ((EditText)findViewById(R.id.reset_email_field)).getText().toString();
            if (!email.isEmpty()) {
                app.getEmailPassword().sendResetPasswordEmailAsync(email, it -> {
                   if (it.isSuccess()) {
                        Toast.makeText(this, "Reset Link Sent To E-mail", Toast.LENGTH_LONG).show();
                       Log.d("Reset Email Link", "SENT");
                   } else {
                       Toast.makeText(this, "E-mail does not exist", Toast.LENGTH_LONG).show();
                       Log.d("Reset Email Link", "NOT SENT: " + it.getError().toString());
                   }
                });
            } else {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }


    boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void initStartButtons() {
//        findViewById(R.id.testbutton).setOnClickListener(v -> {
//            testGetUserInfoLogs();
//        });

        findViewById(R.id.login_button).setOnClickListener(v -> {
            toLogin();
        });
        findViewById(R.id.dont_have_an_account_sign_up).setOnClickListener(v -> {
            toRegister();
        });
        findViewById(R.id.skip_button).setOnClickListener(v -> {

            int finishCode = isForResult() ? finishForResult() : finishWithoutResult();

        });
    }

    public int finishForResult() {
        onBackPressed();
        return 1;
    }

    void logIn() {
        Intent i = new Intent();
        i.putExtra("logged_in", true);
        setResult(Activity.RESULT_OK, i);
        finishForResult();
    }

    public int finishWithoutResult() {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
        return 0;
    }

    public void initLoginButtons() {
        findViewById(R.id.dont_have_an_account_sign_up).setOnClickListener( v-> {
            toRegister();
        });

        findViewById(R.id.forgot_password).setOnClickListener( v-> {
            toReset();
        });

        //add login function
        findViewById(R.id.login_button).setOnClickListener(v -> {
            String loginEmail = ((EditText)findViewById(R.id.login_email_field)).getText().toString();
            String loginPassword = ((EditText) findViewById(R.id.login_password_field)).getText().toString();
            if (!loginEmail.isEmpty() && !loginPassword.isEmpty()) {
                Credentials emailPasswordCredentials = Credentials.emailPassword(loginEmail, loginPassword);
                AtomicReference<User> user = new AtomicReference<User>();
                app.loginAsync(emailPasswordCredentials, it -> {
                    if (it.isSuccess()) {
                        loggedIn = true;
                        Log.d("AUTH", "Successfully authenticated using email and password");
                        user.set(app.currentUser());
                        Log.d("CURR USER", "REFRESH TOKEN: " + app.currentUser().getRefreshToken());
                        Log.d("CURR USER", "ACCESS TOKEN: " + app.currentUser().getAccessToken());
                        toStart();
                        //start intent main activity
                        if (isForResult()) {
                            //finish and pass result back to mainactivity
                            logIn();
                        } else {
                            //start main activity with new intent and pass data
                            startMainLoggedIn();
                        }

                    } else {
                        Toast.makeText(this, it.getError().toString(), Toast.LENGTH_SHORT).show();
                        Log.d("AUTH", it.getError().toString());
                    }
                });

            }

        });
    }
    public void initRegisterButtons() {
        findViewById(R.id.already_have_an_account_login).setOnClickListener( v-> {
            toLogin();
        });
        //add register function
        findViewById(R.id.register_button).setOnClickListener(v -> {
            String registerName = ((EditText)findViewById(R.id.register_name_field)).getText().toString();
            String registerEmail = ((EditText)findViewById(R.id.register_email_field)).getText().toString();
            String registerPassword = ((EditText) findViewById(R.id.register_password_field)).getText().toString();
            String registerConfirmPassword = ((EditText) findViewById(R.id.register_confirm_password_field)).getText().toString();
            if (isValidRegister(registerName, registerEmail, registerPassword, registerConfirmPassword))  {
                resetInvalidFields();
                app.getEmailPassword().registerUserAsync(registerEmail, registerPassword, it -> {
                    if (it.isSuccess()) {
                        Toast.makeText(this, "A confirmation link was sent to your e-mail", Toast.LENGTH_LONG).show();
                        insertAnonData(registerEmail, registerName);
                        toStart();
                        //login as anonymous first , then insert email name data, on confirm link user to data by finding matching email entry
                        Log.d("REGISTER", "SUCCESSFULLY REGISTERED USER");
                    } else {
                        makeToast(it.getError().getErrorMessage().replace("name", "email"), Toast.LENGTH_SHORT);

                        Log.d("REGISTER", "FAILED TO REGISTER USER");
                    }
                });

            } else {
                resetInvalidFields();
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                setInvalidFields(registerName, registerEmail, registerPassword, registerConfirmPassword);
            }

        });
    }
    public void makeToast(String text, int dur) {
        Toast toast = Toast.makeText(this, text, dur);
        toast.show();
    }
    public void resetInvalidFields() {
        ((TextView) findViewById(R.id.invalid_name)).setText("");
        ((TextView) findViewById(R.id.invalid_email)).setText("");
        ((TextView) findViewById(R.id.invalid_password)).setText("");
        ((TextView) findViewById(R.id.invalid_confirmpassword)).setText("");
    }
    public void setInvalidFields(String name, String email, String pass, String confirmPass) {
        if (!isValidName(name)) {
            ((TextView) findViewById(R.id.invalid_name)).setText("Name cannot be blank");
        }
        if (!isEmailValid(email)) {
            ((TextView) findViewById(R.id.invalid_email)).setText("Invalid Email");
        }
        if (!isValidPass(pass)) {
            ((TextView) findViewById(R.id.invalid_password)).setText("Minimum 8 characters. At least one letter, number, and special character");
        }
        if (!confirmPassMatches(pass, confirmPass)) {
            ((TextView) findViewById(R.id.invalid_confirmpassword)).setText("Password does not match");
        }
    }

    private boolean isLoggedIn() {
        return loggedIn;
    }

    boolean isValidRegister(String name, String email, String password, String confirmPassword) {
        return isValidName(name) && isEmailValid(email) && isValidPass(password) && confirmPassMatches(password, confirmPassword);
    }
    boolean confirmPassMatches(String pass, String confirm) {
        return pass.equals(confirm);
    }
    boolean isValidName(String name) {
        return !name.isEmpty();
    }

    void testGetUserInfoLogs() {
        Log.d("CURR USER", "LOGGED IN: " + app.currentUser().isLoggedIn());
        Log.d("CURR USER", "ID: " + app.currentUser().getId());
        Log.d("CURR USER", "ACCESS TOKEN: " + app.currentUser().getAccessToken());
        Log.d("CURR USER", "CUSTOM DATA: " + app.currentUser().getCustomData().toJson());
        Log.d("CURR USER", "REFRESH TOKEN: " + app.currentUser().getRefreshToken());
    }


    void insertAnonData(String email, String name) {
        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
           if (result.isSuccess()) {
               Log.d("ANON USER LOGIN", "SUCCESS");
               User user = app.currentUser();
               MongoClient client = user.getMongoClient("mongodb-atlas");
               MongoDatabase mongoDatabase = client.getDatabase("Figure");
               MongoCollection<Document> users = mongoDatabase.getCollection("Users");
               users.insertOne(new Document("_id", new ObjectId()).append("email", email).append("name", name)).getAsync(result1 -> {
                   if (result1.isSuccess()) {
                       Log.d("INSERT ANON DATA", "SUCCESS");
                       user.logOutAsync(r -> {
                          if (r.isSuccess()) {
                              Log.d("ANON USER LOGOUT", "SUCCESS");
                          } else {
                              Log.d("ANON USER LOGOUT", "FAILED. ERROR: " + r.getError().toString());
                          }
                       });
                   } else {
                       user.logOutAsync(r -> {
                           if (r.isSuccess()) {
                               Log.d("ANON USER LOGOUT", "SUCCESS");
                           } else {
                               Log.d("ANON USER LOGOUT", "FAILED. ERROR: " + r.getError().toString());
                           }
                       });
                       Log.d("INSERT ANON DATA", "FAILED. ERROR: " + result1.getError().toString());
                   }

               });
           } else {
               Log.d("ANON USER LOGIN", "FAILED. ERROR: " + result.getError().toString());
           }
        });
    }


    public static boolean isValidPass(final String pass) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    public void initScenes() {
        set = TransitionInflater.from(this).inflateTransition(R.transition.animate);
        set.setDuration(400);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        startScene = Scene.getSceneForLayout(sceneRoot, R.layout.start_scene, this);
        loginScene = Scene.getSceneForLayout(sceneRoot, R.layout.login_scene, this);
        resetScene = Scene.getSceneForLayout(sceneRoot, R.layout.resetpass_scene, this);
        registerScene = Scene.getSceneForLayout(sceneRoot, R.layout.register_scene, this);

        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                if (currScene == StartScene.LOGIN) {
                    Log.d("ended", "login");
                    initLoginButtons();
                } else if (currScene == StartScene.REGISTER) {
                    Log.d("ended", "register");
                    initRegisterButtons();

                } else if (currScene == StartScene.START) {
                    initStartButtons();
                    if (isLoggedIn()) {
                        // begin loading mainactivity
                    }
                } else if (currScene == StartScene.RESET) {
                    initResetButtons();
                }

            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {

            }
        });


    }
    public void toLogin() {
        Log.d("tologin", "called");
        currScene = StartScene.LOGIN;
        TransitionManager.go(loginScene, set);
    }

    public void toRegister() {
        Log.d("toregister", "called");
        currScene = StartScene.REGISTER;
        TransitionManager.go(registerScene, set);
    }
    public void toStart() {
        currScene = StartScene.START;
        TransitionManager.go(startScene, set);
    }
    public void toReset() {
        currScene = StartScene.RESET;
        TransitionManager.go(resetScene, set);
    }
    @Override
    public void onBackPressed() {
        if (currScene == StartScene.LOGIN || currScene == StartScene.REGISTER || currScene == StartScene.RESET) {
            toStart();
        } else {
            super.onBackPressed();
        }

    }



}

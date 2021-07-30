package com.example.figure;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;

import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    ViewGroup sceneRoot;
    Transition set;
    Scene startScene;
    Scene loginScene;
    Scene registerScene;
    private StartScene currScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_start);
        initScenes();

        findViewById(R.id.login_button).setOnClickListener(v -> {
            toLogin();
        });
        findViewById(R.id.dont_have_an_account_sign_up).setOnClickListener(v -> {
            toRegister();
        });
        currScene = StartScene.START;

    }


    public void initScenes() {
        set = TransitionInflater.from(this).inflateTransition(R.transition.animate);
        set.setDuration(400);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        startScene = Scene.getSceneForLayout(sceneRoot, R.layout.start_scene, this);
        loginScene = Scene.getSceneForLayout(sceneRoot, R.layout.login_scene, this);
        registerScene = Scene.getSceneForLayout(sceneRoot, R.layout.register_scene, this);

        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                if (currScene == StartScene.LOGIN) {
                    Log.d("ended", "login");
                    findViewById(R.id.dont_have_an_account_sign_up).setOnClickListener( v-> {
                        toRegister();
                    });
                } else if (currScene == StartScene.REGISTER) {
                    Log.d("ended", "register");
                    findViewById(R.id.already_have_an_account_login).setOnClickListener( v-> {
                        toLogin();
                    });

                } else if (currScene == StartScene.START) {
                    findViewById(R.id.login_button).setOnClickListener(v -> {
                        toLogin();
                    });
                    findViewById(R.id.dont_have_an_account_sign_up).setOnClickListener(v -> {
                        toRegister();
                    });

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
    @Override
    public void onBackPressed() {
        if (currScene == StartScene.LOGIN || currScene == StartScene.REGISTER) {
            toStart();
        } else {
            super.onBackPressed();
        }

    }



}

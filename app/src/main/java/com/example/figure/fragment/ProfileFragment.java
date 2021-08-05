package com.example.figure.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;

import com.example.figure.view.NutritionDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mikhaellopez.circularimageview.*;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.example.figure.MainActivity;
import com.example.figure.R;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.util.Random;

public class ProfileFragment extends Fragment implements NutritionDialog.SetListener {
    Context context;
    View _rootView;
    CircularImageView circularImageView;
    TextView name;
    TextView heightView;
    TextView weightView;
    TextView goalWeightView;
    ImageButton editButton;
    CircularProgressIndicator calProgressBar;
    CircularProgressIndicator fatProgressBar;
    CircularProgressIndicator carbProgressBar;
    CircularProgressIndicator proteinProgressBar;
    TextView calories;
    TextView fat;
    TextView carbs;
    TextView protein;
    TextView goalCalories;
    TextView goalFat;
    TextView goalCarbs;
    TextView goalProtein;

    //    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }
    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setRetainInstance(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.profile_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (circularImageView == null) {
            initViews(view);
            //setGoalCalories(2300);
//            calProgressBar.setOnClickListener(v -> {
//
//                Random rand = new Random();
//                int cal = rand.nextInt(2300);
//                setCalories(cal);
//                //setGoalCalories(cal);
//            });
        }
        //setProfilePic(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "profileFragment";
    }


    void initViews(View view) {
        circularImageView = (CircularImageView) view.findViewById(R.id.profile_image);
        name = (TextView) view.findViewById(R.id.profile_name);
        heightView = (TextView) view.findViewById(R.id.profile_height);
        weightView = (TextView) view.findViewById(R.id.profile_weight);
        goalWeightView = (TextView) view.findViewById(R.id.profile_goal_weight);
        editButton = (ImageButton) view.findViewById(R.id.edit_profile_button);
        calProgressBar = (CircularProgressIndicator) view.findViewById(R.id.calories_progress_bar);
        fatProgressBar = (CircularProgressIndicator) view.findViewById(R.id.fat_progress_bar);
        carbProgressBar = (CircularProgressIndicator) view.findViewById(R.id.carbs_progress_bar);
        proteinProgressBar = (CircularProgressIndicator) view.findViewById(R.id.protein_progress_bar);
        calories = (TextView) view.findViewById(R.id.calories);
        fat = (TextView) view.findViewById(R.id.fat);
        carbs = (TextView) view.findViewById(R.id.carbs);
        protein = (TextView) view.findViewById(R.id.protein);
        goalCalories = (TextView) view.findViewById(R.id.goal_calories);
        goalFat = (TextView) view.findViewById(R.id.goal_fat);
        goalCarbs = (TextView) view.findViewById(R.id.goal_carbs);
        goalProtein = (TextView) view.findViewById(R.id.goal_protein);

        calProgressBar.setOnClickListener(v -> {
            openDialog(0);
        });
        fatProgressBar.setOnClickListener(v -> {
            openDialog(1);
        });
        carbProgressBar.setOnClickListener(v -> {
            openDialog(2);
        });
        proteinProgressBar.setOnClickListener(v -> {
            openDialog(3);
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.omelette);
        circularImageView.setImageBitmap(bitmap);


    }
    void setProfilePic(Bitmap bitmap) {
        circularImageView.setImageBitmap(bitmap);
//        ShapeableImageView profileImageView = (ShapeableImageView) view.findViewById(R.id.profile_image);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.omelette);
//        profileImageView.setImageBitmap(resizeProfileImage(bitmap));
    }
    public Bitmap getProfilePic() {
        return ((BitmapDrawable)circularImageView.getDrawable()).getBitmap();
    }

    private Bitmap resizeProfileImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int minwidth = dpToPx(200, context);
        int minheight = dpToPx(200, context);
        float scaleWidth = ((float) minwidth)/width;
        float scaleHeight = ((float) minheight)/height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        Bitmap crop = getCircleCrop(resizedBitmap);
        return crop;

    }
    private Bitmap getCircleCrop(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    void setHeight(int feet, int inches) {
        heightView.setText(feet + "'" + inches);
    }
    void setWeight(int pounds) {
        weightView.setText(pounds);
    }
    void setGoalWeight(int pounds) {
        goalWeightView.setText(pounds);
    }
    void setName(String name) {
        this.name.setText(name);
    }
    void setProfilePic(Bitmap bitmap) {
        circularImageView.setImageBitmap(bitmap);
    }

    void setGoalCalories(int goalCalories) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.goalCalories.getText().toString()), goalCalories);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.goalCalories.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //this.goalCalories.setText(Integer.toString(goalCalories));
        this.calProgressBar.setMax(goalCalories);
    }
    public void setCalories(int cal) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(calories.getText().toString()), cal);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            calories.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //calories.setText("" + cal);
        //calProgressBar.setProgress(cal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calProgressBar.setProgress(cal, true);
        } else {
            ObjectAnimator o = ObjectAnimator.ofInt(calProgressBar, "progress", cal);
            o.setInterpolator(new AccelerateDecelerateInterpolator());
            o.setDuration(700).start();
            //ObjectAnimator.ofInt(calProgressBar, "progress", cal).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }
    }
    void setGoalFat(int goalFat) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.goalFat.getText().toString()), goalFat);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.goalFat.setText(animation.getAnimatedValue().toString());
        });
        animator.start();

        //this.goalFat.setText("" + goalFat);
        this.fatProgressBar.setMax(goalFat);
    }
    void setGoalCarbs(int goalCarbs) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.goalCarbs.getText().toString()), goalCarbs);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.goalCarbs.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //this.goalCarbs.setText("" + goalCarbs);
        this.carbProgressBar.setMax(goalCarbs);
    }
    void setGoalProtein(int goalProtein) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.goalProtein.getText().toString()), goalProtein);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.goalProtein.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //this.goalProtein.setText("" + goalProtein);
        this.proteinProgressBar.setMax(goalProtein);
    }

    void setFat(int fat) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.fat.getText().toString()), fat);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.fat.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //this.fat.setText("" + fat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.fatProgressBar.setProgress(fat, true);
        } else {
            ObjectAnimator.ofInt(this.fatProgressBar, "progress", fat).setDuration(300).start();
        }
    }
    void setCarbs(int carbs) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.carbs.getText().toString()), carbs);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.carbs.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //this.carbs.setText("" + carbs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.carbProgressBar.setProgress(carbs, true);
        } else {
            ObjectAnimator.ofInt(this.carbProgressBar, "progress", carbs).setDuration(300).start();
        }
    }
    void setProtein(int protein) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(this.protein.getText().toString()), protein);
        animator.setDuration(700);
        animator.addUpdateListener(animation -> {
            this.protein.setText(animation.getAnimatedValue().toString());
        });
        animator.start();
        //this.protein.setText("" + protein);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.proteinProgressBar.setProgress(protein, true);
        } else {
            ObjectAnimator.ofInt(this.proteinProgressBar, "progress", protein).setDuration(300).start();
        }
    }

    public String getCalories() {
        return calories.getText().toString();
    }
    public String getFat() {
        return fat.getText().toString();
    }
    public String getCarbs() {
        return carbs.getText().toString();
    }
    public String getProtein() {
        return protein.getText().toString();
    }
    public String getGoalCalories() {
        return goalCalories.getText().toString();
    }
    public String getGoalFat() {
        return goalFat.getText().toString();
    }
    public String getGoalCarbs() {
        return goalCarbs.getText().toString();
    }
    public String getGoalProtein() {
        return goalProtein.getText().toString();
    }

    void openDialog(int i) {
        NutritionDialog dialog = new NutritionDialog(i);
        dialog.show(getChildFragmentManager(), "nutrition dialog");
    }


    private static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void setValues(String currVal, String goalVal, String mode) {
        if (mode.equalsIgnoreCase("calories")) {
            setGoalCalories(Integer.parseInt(goalVal));
            setCalories(Integer.parseInt(currVal));
        } else if (mode.equalsIgnoreCase("fat")) {
            setGoalFat(Integer.parseInt(goalVal));
            setFat(Integer.parseInt(currVal));
        } else if (mode.equalsIgnoreCase("carbs")) {
            setGoalCarbs(Integer.parseInt(goalVal));
            setCarbs(Integer.parseInt(currVal));
        } else if (mode.equalsIgnoreCase("protein")) {
            setGoalProtein(Integer.parseInt(goalVal));
            setProtein(Integer.parseInt(currVal));
        }
    }
}

package com.example.figure.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;


import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;

import com.example.figure.data.ProfileModel;
import com.example.figure.view.NutritionDialog;
import com.example.figure.view.ProfileStatDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mikhaellopez.circularimageview.*;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.example.figure.MainActivity;
import com.example.figure.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Aggregates;

import org.bson.BSONObject;
import org.bson.BsonDocument;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import io.realm.RealmDictionary;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class ProfileFragment extends Fragment implements NutritionDialog.SetListener, ProfileStatDialog.SetProfileStatsListener {
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

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            try {
                final Uri imageUri = result.getData().getData();
                final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                setProfilePic(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_LONG).show();
        }
    });

    public void openProfileImageChooserActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (circularImageView == null) {
            initViews(view);
            upsertProfile();
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
        circularImageView = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.profile_name);
        heightView = view.findViewById(R.id.profile_height);
        weightView = view.findViewById(R.id.profile_weight);
        goalWeightView = view.findViewById(R.id.profile_goal_weight);
        editButton = view.findViewById(R.id.edit_profile_button);
        calProgressBar = view.findViewById(R.id.calories_progress_bar);
        fatProgressBar = view.findViewById(R.id.fat_progress_bar);
        carbProgressBar = view.findViewById(R.id.carbs_progress_bar);
        proteinProgressBar = view.findViewById(R.id.protein_progress_bar);
        calories = view.findViewById(R.id.calories);
        fat = view.findViewById(R.id.fat);
        carbs = view.findViewById(R.id.carbs);
        protein = view.findViewById(R.id.protein);
        goalCalories = view.findViewById(R.id.goal_calories);
        goalFat = view.findViewById(R.id.goal_fat);
        goalCarbs = view.findViewById(R.id.goal_carbs);
        goalProtein = view.findViewById(R.id.goal_protein);

        calProgressBar.setOnClickListener(v -> {
            openNutritionDialog(0);
        });
        fatProgressBar.setOnClickListener(v -> {
            openNutritionDialog(1);
        });
        carbProgressBar.setOnClickListener(v -> {
            openNutritionDialog(2);
        });
        proteinProgressBar.setOnClickListener(v -> {
            openNutritionDialog(3);
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.omelette);
        setProfilePic(bitmap);

        editButton.setOnClickListener(v -> {
            openStatDialog();
        });
        circularImageView.setOnClickListener(v -> {
           openProfileImageChooserActivity();
        });

    }

    void openStatDialog() {
        ProfileStatDialog dialog = new ProfileStatDialog();
        dialog.show(getChildFragmentManager(), "stat dialog");
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

//    private Bitmap resizeProfileImage(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int minwidth = dpToPx(200, context);
//        int minheight = dpToPx(200, context);
//        float scaleWidth = ((float) minwidth)/width;
//        float scaleHeight = ((float) minheight)/height;
//
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
//        bitmap.recycle();
//        Bitmap crop = getCircleCrop(resizedBitmap);
//        return crop;
//
//    }
//    private Bitmap getCircleCrop(Bitmap bitmap) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0,0,0,0);
//        paint.setColor(color);
//        canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        return output;
//    }
    public void setHeight(String height) {
        heightView.setText(height);
    }
    public void setHeight(int feet, int inches) {
        heightView.setText(feet + "'" + inches);
    }
    public void setWeight(int pounds) {
        weightView.setText("" + pounds);
    }
    public void setWeight(String weight) {
        weightView.setText(weight);
    }
    public void setGoalWeight(String goalWeight) {
        goalWeightView.setText(goalWeight);
    }
    public void setGoalWeight(int pounds) {
        goalWeightView.setText("" + pounds);
    }
    public void setName(String name) {
        this.name.setText(name);
    }
//    void setProfilePic(Bitmap bitmap) {
//        circularImageView.setImageBitmap(bitmap);
//    }

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

    public String getName() {
        return name.getText().toString();
    }
    public String getHeight() {
        return heightView.getText().toString();
    }
    public String getWeight() {
        return weightView.getText().toString();
    }
    public String getGoalWeight() {
        return goalWeightView.getText().toString();
    }
    public int getFeet() {
        return Integer.parseInt(getHeight().split("'")[0]);
    }
    public int getInch() {
        return Integer.parseInt(getHeight().split("'")[1]);
    }

    void openNutritionDialog(int i) {
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

    void upsertProfile() {
        //test profile
        ProfileModel profile = new ProfileModel();
        profile.setCalories(2000);
        profile.setCarbs(100);
        profile.setFat(100);
        profile.setProtein(100);
        profile.setGoalCalories(2300);
        profile.setGoalCarbs(300);
        profile.setGoalFat(300);
        profile.setGoalProtein(300);
        profile.setGoalWeight(200);
        profile.setWeight(150);
        profile.setName("Colin");
        HashMap<String, Integer> height = new HashMap<>();
        height.put("feet", 5);
        height.put("inch", 11);
        profile.setHeight(height);
        BasicDBObject s = new BasicDBObject();
        s.append("profile", profile);

        App app = new App(new AppConfiguration.Builder("figure-mdlbd").build());
        User user = app.currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = client.getDatabase("Figure");
        CodecRegistry registry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoCollection<Document> users = mongoDatabase.getCollection("Users");

        Document prof = new Document();
        prof.put("Name", profile.getName());
        prof.put("calories", profile.getCalories());
        prof.put("height", profile.getHeight());

        Document doc = new Document("profile", prof);



        users.findOneAndUpdate(new Document("profile", "uwu"), new Document("$set", prof)).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("UPDATE", "SUCCESS");
            } else {
                Log.d("UPDATE", result.getError().toString());
            }
        });

    }

    @Override
    public void setStats(String name, String height, String weight, String goalWeight) {
        setName(name);
        setHeight(height);
        setWeight(weight);
        setGoalWeight(goalWeight);
    }

}

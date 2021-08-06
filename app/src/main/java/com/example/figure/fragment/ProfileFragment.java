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
            //upsertProfile();
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

    Document getProfileDocument() {
        App app = new App(new AppConfiguration.Builder("figure-mdlbd").build());
        User user = app.currentUser();
        return user.getCustomData();
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
        getValueAnimator(this.goalCalories, goalCalories, 700).start();
        //this.goalCalories.setText(Integer.toString(goalCalories));
        //this.calProgressBar.setMax(goalCalories);
        animateProgressMax(this.calProgressBar, goalCalories, 700);
    }
    public void setCalories(int cal) {
        getValueAnimator(this.calories, cal, 700).start();
        //calories.setText("" + cal);
        //calProgressBar.setProgress(cal);
        animateProgress(this.calProgressBar, cal, 700);
    }
    void setGoalFat(int goalFat) {
        getValueAnimator(this.goalFat, goalFat, 700).start();

        //this.goalFat.setText("" + goalFat);
        //this.fatProgressBar.setMax(goalFat);
        animateProgressMax(this.fatProgressBar, goalFat, 700);
    }
    void setGoalCarbs(int goalCarbs) {
        getValueAnimator(this.goalCarbs, goalCarbs, 700).start();
        //this.goalCarbs.setText("" + goalCarbs);
        //this.carbProgressBar.setMax(goalCarbs);
        animateProgressMax(this.carbProgressBar, goalCarbs, 700);
    }
    void setGoalProtein(int goalProtein) {
        getValueAnimator(this.goalProtein, goalProtein, 700).start();
        //this.goalProtein.setText("" + goalProtein);
        //this.proteinProgressBar.setMax(goalProtein);
        animateProgressMax(this.proteinProgressBar, goalProtein, 700);
    }

    void setFat(int fat) {
        getValueAnimator(this.fat, fat, 700).start();
        //this.fat.setText("" + fat);
        animateProgress(this.fatProgressBar, fat, 700);
    }
    void setCarbs(int carbs) {
        getValueAnimator(this.carbs, carbs, 700).start();
        //this.carbs.setText("" + carbs);
        animateProgress(this.carbProgressBar, carbs, 700);
    }
    void setProtein(int protein) {
        getValueAnimator(this.protein, protein, 700).start();
        animateProgress(this.proteinProgressBar, protein, 700);
    }

    void animateProgress(CircularProgressIndicator progressIndicator, int val, int dur) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressIndicator.setProgress(val, true);
        } else {
            ObjectAnimator o = ObjectAnimator.ofInt(progressIndicator, "progress", val);
            o.setInterpolator(new AccelerateDecelerateInterpolator());
            o.setDuration(dur).start();
        }
    }
    void animateProgressMax(CircularProgressIndicator progressIndicator, int max, int dur) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressIndicator.setMax(max);
        } else {
            ObjectAnimator o = ObjectAnimator.ofInt(progressIndicator, "max", max);
            o.setInterpolator(new AccelerateDecelerateInterpolator());
            o.setDuration(dur).start();
        }
    }

    ValueAnimator getValueAnimator(TextView textView, int value, int dur) {
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(textView.getText().toString()), value).setDuration(dur);
        animator.addUpdateListener(animation -> {
            textView.setText(animation.getAnimatedValue().toString());
        });
        return animator;
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


//    private static int dpToPx(float dp, Context context) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
//    }

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

    ProfileModel createProfileObject() {
        HashMap<String, Integer> height = new HashMap<>();
        height.put("feet", getFeet());
        height.put("inch", getInch());
        return new ProfileModel(getName(), Integer.parseInt(getCalories()), Integer.parseInt(getGoalCalories()), Integer.parseInt(getFat()), Integer.parseInt(getGoalFat()),
                Integer.parseInt(getCarbs()), Integer.parseInt(getGoalCarbs()), Integer.parseInt(getProtein()), Integer.parseInt(getGoalProtein()), Integer.parseInt(getWeight()),
                Integer.parseInt(getGoalWeight()), height, getProfilePic());
    }

    void upsertProfile() {

        App app = new App(new AppConfiguration.Builder("figure-mdlbd").build());
        User user = app.currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = client.getDatabase("Figure");
        CodecRegistry registry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoCollection<ProfileModel> users = mongoDatabase.getCollection("Users", ProfileModel.class).withCodecRegistry(registry);


        users.findOneAndUpdate(new Document("userid", user.getId()), new Document("$set", createProfileObject().convertToDocument())).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("UPDATE", "SUCCESS");
                user.refreshCustomData(result1 -> {
                    if (result1.isSuccess()) {
                        Log.d("REFRESH CUSTOMDATA", "REFRESHED");
                    } else {
                        Log.d("REFRESH CUSTOMDATA", "FAILED: " + result1.getError().toString());
                    }
                });
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

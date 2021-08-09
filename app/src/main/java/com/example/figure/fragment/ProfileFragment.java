package com.example.figure.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;



import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.fragment.app.Fragment;

import com.example.figure.MainActivity;
import com.example.figure.R;

import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
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
    private int EXTERNAL_STORAGE_PERMISSION_CODE = 23;

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

    ActivityResultLauncher<Intent> imageChooseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            try {
//                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        EXTERNAL_STORAGE_PERMISSION_CODE);
                final Uri imageUri = result.getData().getData();
                final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                setProfilePic(selectedImage);
                upsertData("image_bitmap_string", bitmapToString(selectedImage));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_LONG).show();
        }
    });



    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result) {
                    openProfileImageChooserActivity();
                    Log.e("READEXTERNALSTORAGE", "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e("READEXTERNALSTORAGE", "onActivityResult: PERMISSION DENIED");
                }
            });



    public void openProfileImageChooserActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imageChooseLauncher.launch(intent);
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (circularImageView == null) {
            initViews(view);
            if (MainActivity.isLoggedIn()) {
                loadProfile();

            }
            //loadProfile();
            //upsertProfile();
        }
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
            mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
//           openProfileImageChooserActivity();
        });

    }

    void openStatDialog() {
        ProfileStatDialog dialog = new ProfileStatDialog();
        dialog.show(getChildFragmentManager(), "stat dialog");
    }

    String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,boas);
        byte[] b = boas.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    void setProfilePic(Bitmap bitmap) {
        circularImageView.setImageBitmap(bitmap);
    }
    public Bitmap getProfilePic() {
        return ((BitmapDrawable)circularImageView.getDrawable()).getBitmap();
    }

//    Document getUserDocument() {
//        App app = new App(new AppConfiguration.Builder("figure-mdlbd").build());
//        User user = app.currentUser();
//        MongoClient client = user.getMongoClient("mongodb-atlas");
//        MongoDatabase mongoDatabase = client.getDatabase("Figure");
//        MongoCollection<Document> users = mongoDatabase.getCollection("Users");
//        users.findOne(new Document("userid", user.getId())).getAsync(result -> {
//            if (result.isSuccess()) {
//
//            } else {
//
//            }
//        });
//        return users.findOne(new Document("userid", user.getId())).get();
//        //return user.getCustomData();
//    }

    Document getProfile(Document user) {
        return (Document) user.get("profile");
    }
    String fetchImageBitmapString(Document profile) {
        return profile.getString("image_bitmap_string");
    }

    Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void loadProfile() {
        App app = new App(new AppConfiguration.Builder("figure-mdlbd").build());
        User user = app.currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = client.getDatabase("Figure");
        MongoCollection<Document> users = mongoDatabase.getCollection("Users");

        users.findOne(new Document("userid", user.getId())).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("FETCH USER", "SUCCESS");
                Document userDoc = result.get();
                Document profile = getProfile(userDoc);
                Document height = (Document) profile.get("height");
                //profile pic
                String bitmapstring = fetchImageBitmapString(profile);
                if (!bitmapstring.isEmpty()) {
                    Bitmap profilePicBitmap = StringToBitmap(bitmapstring);
                    setProfilePic(profilePicBitmap);
                    circularImageView.setBackgroundColor(Color.TRANSPARENT);

                }
                //name
                setName(userDoc.getString("name"));
                //height
                setHeight(height.getInteger("feet"), height.getInteger("inch"));
                //weight
                setWeight(profile.getInteger("weight"));
                //goalweight
                setGoalWeight(profile.getInteger("goal_weight"));
                //goalcalories
                setGoalCalories(profile.getInteger("goal_calories"));
                //calories
                setCalories(profile.getInteger("calories"));
                //goalfat
                setGoalFat(profile.getInteger("goal_fat"));
                //fat
                setFat(profile.getInteger("fat"));
                //goalcarbs
                setGoalCarbs(profile.getInteger("goal_carbs"));
                //carbs
                setCarbs(profile.getInteger("carbs"));
                //goalprotein
                setGoalProtein(profile.getInteger("goal_protein"));
                //protein
                setProtein(profile.getInteger("protein"));

            } else {
                Log.d("FETCH USER", "FAILED: " + result.getError().toString());
            }
        });

    }

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

    void setGoalCalories(int goalCalories) {
        getValueAnimator(this.goalCalories, goalCalories, 700).start();
        animateProgressMax(this.calProgressBar, goalCalories, 700);
    }
    public void setCalories(int cal) {
        getValueAnimator(this.calories, cal, 700).start();
        animateProgress(this.calProgressBar, cal, 700);
    }
    void setGoalFat(int goalFat) {
        getValueAnimator(this.goalFat, goalFat, 700).start();

        animateProgressMax(this.fatProgressBar, goalFat, 700);
    }
    void setGoalCarbs(int goalCarbs) {
        getValueAnimator(this.goalCarbs, goalCarbs, 700).start();
        animateProgressMax(this.carbProgressBar, goalCarbs, 700);
    }
    void setGoalProtein(int goalProtein) {
        getValueAnimator(this.goalProtein, goalProtein, 700).start();
        animateProgressMax(this.proteinProgressBar, goalProtein, 700);
    }

    void setFat(int fat) {
        getValueAnimator(this.fat, fat, 700).start();
        animateProgress(this.fatProgressBar, fat, 700);
    }
    void setCarbs(int carbs) {
        getValueAnimator(this.carbs, carbs, 700).start();
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

    @Override
    public void setValues(String currVal, String goalVal, String mode) {
        int v = Integer.parseInt(currVal);
        int gV = Integer.parseInt(goalVal);
        if (mode.equalsIgnoreCase("calories")) {
            setGoalCalories(gV);
            setCalories(v);

            upsertData("calories", v);
            upsertData("goal_calories", gV);
        } else if (mode.equalsIgnoreCase("fat")) {
            setGoalFat(gV);
            setFat(v);

            upsertData("fat", v);
            upsertData("goal_fat", gV);
        } else if (mode.equalsIgnoreCase("carbs")) {
            setGoalCarbs(gV);
            setCarbs(v);

            upsertData("carbs", v);
            upsertData("goal_carbs", gV);
        } else if (mode.equalsIgnoreCase("protein")) {
            setGoalProtein(gV);
            setProtein(v);
            upsertData("protein", v);
            upsertData("goal_protein", gV);
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

    void upsertData(String field, String data) {
        App app = getApp();
        User user = getUser(app);
        MongoCollection<Document> users = getCollection(user);

        users.findOneAndUpdate(new Document("userid", user.getId()), new Document("$set", new Document("profile." + field, data))).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("UPSERT " + field, "SUCCESS");
            } else {
                Log.d("UPSERT " + field, "FAILED: " + result.getError().toString());
            }
        });

    }
    void upsertData(String field, Document data) {
        App app = getApp();
        User user = getUser(app);
        MongoCollection<Document> users = getCollection(user);

        users.findOneAndUpdate(new Document("userid", user.getId()), new Document("$set", new Document("profile." + field, data))).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("UPSERT " + field, "SUCCESS");
            } else {
                Log.d("UPSERT " + field, "FAILED: " + result.getError().toString());
            }
        });

    }
    void upsertData(String field, int data) {
        App app = getApp();
        User user = getUser(app);
        MongoCollection<Document> users = getCollection(user);

        users.findOneAndUpdate(new Document("userid", user.getId()), new Document("$set", new Document("profile." + field, data))).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("UPSERT " + field, "SUCCESS");
            } else {
                Log.d("UPSERT " + field, "FAILED: " + result.getError().toString());
            }
        });

    }

    void upsertName(String newName) {
        App app = getApp();
        User user = getUser(app);
        MongoCollection<Document> users = getCollection(user);

        users.findOneAndUpdate(new Document("userid", user.getId()), new Document("$set", new Document("name", newName))).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("UPSERT NAME", "SUCCESS");
            } else {
                Log.d("UPSERT NAME", "FAILED: " + result.getError().toString());
            }
        });
    }

    private App getApp() {
        return new App(new AppConfiguration.Builder("figure-mdlbd").build());
    }
    private User getUser(App app) {
        return app.currentUser();
    }
    private MongoCollection<Document> getCollection(User user) {
        return user.getMongoClient("mongodb-atlas").getDatabase("Figure").getCollection("Users");
    }


    public void upsertProfile() {

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
            } else {
                Log.d("UPDATE", result.getError().toString());
            }
        });

    }



    @Override
    public void setStats(String name, String height, String weight, String goalWeight) {
        setName(name);
        upsertName(name);
        setHeight(height);
        String[] feetInch = height.split("'");
        Document h = new Document("feet", Integer.parseInt(feetInch[0]));
        h.put("inch", Integer.parseInt(feetInch[1]));
        upsertData("height", h);
        setWeight(weight);
        upsertData("weight", Integer.parseInt(weight));
        setGoalWeight(goalWeight);
        upsertData("goal_weight", Integer.parseInt(goalWeight));
    }

}

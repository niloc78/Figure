package com.example.figure;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.figure.adapter.MainPagerAdapter;
import com.example.figure.fragment.MainFragment;
import com.example.figure.view.OneDirectionViewPager;

public class MainActivity extends FragmentActivity implements FragmentChangeListener {
    public static OneDirectionViewPager viewPager;
    public Integer colorFrom;
    public Integer colorTo;
    public ValueAnimator colorAnim;
    public MainActivity() {
        super(R.layout.activity_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.backgroundgray));

        if (savedInstanceState == null) {
              colorFrom = getWindow().getStatusBarColor();
              colorTo = ContextCompat.getColor(MainActivity.this, R.color.cook_orange);
              colorAnim = ValueAnimator.ofArgb(colorFrom, colorTo);
              colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                  @Override
                  public void onAnimationUpdate(ValueAnimator animation) {
                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                          getWindow().setStatusBarColor((Integer) animation.getAnimatedValue());
                      }
                  }
              });
              colorAnim.setDuration(900);
              colorAnim.setStartDelay(0);
//            DashFragment dashFrag = new DashFragment();
//
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container, dashFrag, dashFrag.toString())
//                    .commit();
            viewPager = (OneDirectionViewPager) findViewById(R.id.pager);
            final MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), 2);

            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 1) {
                        if (MainFragment.modePager.getCurrentItem() == 0) {
                            colorFrom = getWindow().getStatusBarColor();
                            colorTo = ContextCompat.getColor(MainActivity.this, R.color.cook_orange);

                            colorAnim.setIntValues(colorFrom, colorTo);
                            colorAnim.start();

                        } else if (MainFragment.modePager.getCurrentItem() == 1) {
                            colorFrom = getWindow().getStatusBarColor();
                            colorTo = ContextCompat.getColor(MainActivity.this, R.color.dine_blue);

                            colorAnim.setIntValues(colorFrom, colorTo);
                            colorAnim.start();

                        } else if (MainFragment.modePager.getCurrentItem() == 2) {
                            colorFrom = getWindow().getStatusBarColor();
                            colorTo = ContextCompat.getColor(MainActivity.this, R.color.delivery_green);

                            colorAnim.setIntValues(colorFrom, colorTo);
                            colorAnim.start();

                        }

                        viewPager.setAllowedSwipeDirection(SwipeDirection.NONE);

                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }




    @Override
    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.detach(fragmentManager.findFragmentByTag("dashFragment"));
//        fragmentTransaction.add(R.id.fragment_container, fragment, "mainFragment");
//        fragmentTransaction.addToBackStack("transition");
//
//        fragmentTransaction.commit();

    }



    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (viewPager.getCurrentItem() == 0) {
                moveTaskToBack(true);
            } else {
                colorFrom = getWindow().getStatusBarColor();
                colorTo = ContextCompat.getColor(MainActivity.this, R.color.backgroundgray);

                colorAnim.setIntValues(colorFrom, colorTo);
                colorAnim.start();

                viewPager.setCurrentItem(0);
                viewPager.setAllowedSwipeDirection(SwipeDirection.ALL);
            }

        } else {
            super.onBackPressed();
        }

    }

}
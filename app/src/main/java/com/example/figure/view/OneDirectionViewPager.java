package com.example.figure.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.figure.SwipeDirection;

public class OneDirectionViewPager extends ViewPager {
    private float initialXValue;
    private SwipeDirection direction;

    public OneDirectionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.ALL;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {

        if(direction == SwipeDirection.NONE)//disable any swipe
            return false;

        if(this.direction == SwipeDirection.ALL) return true;



        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.RIGHT ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && direction == SwipeDirection.LEFT ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }


}

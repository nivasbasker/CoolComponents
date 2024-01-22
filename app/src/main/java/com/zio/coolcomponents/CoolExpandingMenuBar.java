package com.zio.coolcomponents;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;

import java.util.ArrayList;
import java.util.List;

public class CoolExpandingMenuBar extends RelativeLayout {

    private static final String TAG = "CoolComponents";
    private LinearLayout itemsLayout;
    private CoolIcons currentItem;


    private final Context context;
    private int menuResource = -1;
    private OnItemSelectedListener listener;


    boolean isExpanded = false;
    private final List<CoolIcons> iconsList = new ArrayList<>();

    public CoolExpandingMenuBar(Context context, @NonNull int menuResourceId) {
        super(context);
        Log.i(TAG, "creating CoolExpandingMenuBar");
        this.context = context;
        this.menuResource = menuResourceId;

        init();
    }

    public CoolExpandingMenuBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "creating CoolExpandingMenuBar");
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoolExpandingMenuBar);
        menuResource = a.getResourceId(R.styleable.CoolExpandingMenuBar_menuResource, -1);
        a.recycle();

        init();
    }

    private void init() {
        Log.i(TAG, "rendering layout");
        inflate(getContext(), R.layout.cool_expanding_menu_bar, this);

        itemsLayout = findViewById(R.id.items_layout);
        currentItem = findViewById(R.id.selected_item);

        currentItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Togglemenu();
            }
        });

        if (menuResource != -1) {
            // Inflate the menu XML resource
            MenuInflater inflater = new MenuInflater(getContext());
            Menu menu = new MenuBuilder(getContext());
            inflater.inflate(menuResource, menu);

            currentItem.setMenuItem(menu.getItem(0));
            //listener.OnMenuItemSelected(menu.getItem(0));

            if (menu.size() >5)
                Log.w(TAG, "too long menu, not recommended for a navigation bar, truncating to max limit of 5");

            for (int i = 0; i < min(menu.size(), 5); i++) {

                MenuItem menuItem = menu.getItem(i);
                CoolIcons icon = new CoolIcons(context, menuItem);
                iconsList.add(icon);
                itemsLayout.addView(icon);


                icon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //collapse
                        Togglemenu();

                        selectItem(icon);
                        //listener for users
                        if (listener != null) listener.OnMenuItemSelected(menuItem);

                    }
                });
            }

        } else {
            Log.e(TAG, "menu resource is not attached");
        }


    }

    private void selectItem(CoolIcons icon) {
        if (icon.getMenuItem().getItemId() == currentItem.getMenuItem().getItemId()) return;

        currentItem.setMenuItem(icon.getMenuItem());
    }

    public int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void Togglemenu() {

        if (!isExpanded) {
            itemsLayout.setVisibility(VISIBLE);
            Animation open_anuim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -1.0f,   // fromXDelta: Start from 0% of view width
                    Animation.RELATIVE_TO_SELF, 0.0f,   // toXDelta: Move to 100% of view width
                    Animation.RELATIVE_TO_SELF, 0.0f,   // fromYDelta: Start from 0% of view height
                    Animation.RELATIVE_TO_SELF, 0.0f    // toYDelta: Stay at 0% of view height
            );

            // Set the animation duration
            open_anuim.setDuration(1000);  // Adjust the duration as needed (in milliseconds)

            // Start the animation
            itemsLayout.startAnimation(open_anuim);
        } else {
            Animation open_anuim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,   // fromXDelta: Start from 0% of view width
                    Animation.RELATIVE_TO_SELF, -1.0f,   // toXDelta: Move to 100% of view width
                    Animation.RELATIVE_TO_SELF, 0.0f,   // fromYDelta: Start from 0% of view height
                    Animation.RELATIVE_TO_SELF, 0.0f    // toYDelta: Stay at 0% of view height
            );

            // Set the animation duration
            open_anuim.setDuration(1000);  // Adjust the duration as needed (in milliseconds)

            // Start the animation
            itemsLayout.startAnimation(open_anuim);
            itemsLayout.setVisibility(INVISIBLE);

        }

        isExpanded = !isExpanded;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void OnMenuItemSelected(MenuItem item);
    }
}

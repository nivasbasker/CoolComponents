package com.zio.coolcomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class CoolIcons extends RelativeLayout {

    //views
    private TextView labelView;
    private ImageView iconView;
    private RelativeLayout icon_layout;


    private Context context;

    //data
    private String labelText;
    private Drawable iconImg;
    boolean active = false;
    private MenuItem menuItem = null;

    public CoolIcons(Context context, MenuItem menuItem) {
        super(context);
        this.menuItem = menuItem;

        labelText = menuItem.getTitle().toString();
        iconImg = menuItem.getIcon();
        active = false;

        init();

    }

    public CoolIcons(Context context, String label, Drawable iconDrawable, boolean isActive) {
        super(context);
        this.context = context;

        labelText = label;
        iconImg = iconDrawable;
        active = isActive;
        init();
    }

    public CoolIcons(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoolIcons);

        labelText = a.getNonResourceString(R.styleable.CoolIcons_label);
        active = a.getBoolean(R.styleable.CoolIcons_active, false);
        iconImg = ResourcesCompat.getDrawable(getResources(),
                a.getResourceId(R.styleable.CoolIcons_icon, -1),
                context.getTheme());
        a.recycle();

        init();
    }


    private void init() {
        inflate(getContext(), R.layout.cool_icon, this);

        labelView = findViewById(R.id.icon_label);
        iconView = findViewById(R.id.icon_img);
        icon_layout = findViewById(R.id.icon_layout);

        labelView.setText(labelText);
        iconView.setImageDrawable(iconImg);
        setActive(active);
    }

    public void setActive(boolean active) {
        this.setSelected(active);
        if (!active) {
            labelView.setVisibility(VISIBLE);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            icon_layout.setBackgroundResource(R.color.transparent);
            icon_layout.setLayoutParams(params);

        } else {
            labelView.setVisibility(GONE);
            LayoutParams params = new LayoutParams(dpToPx(70), dpToPx(70));
            icon_layout.setBackgroundResource(R.drawable.background_selected_menu_item);
            icon_layout.setLayoutParams(params);
        }
    }

    public int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;

        labelText = menuItem.getTitle().toString();
        iconImg = menuItem.getIcon();

        labelView.setText(labelText);
        iconView.setImageDrawable(iconImg);
    }


}

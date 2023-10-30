package com.mpiannucci.reactnativecontextmenu.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.content.res.AppCompatResources;

import com.mpiannucci.reactnativecontextmenu.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPopupWindow extends PopupWindow {
    private final LinearLayout mContainer;
    private final FrameLayout mContent;

    public void setContentView(@Nullable View contentView) {
        if (contentView != null) {
            this.mContainer.removeAllViews();
            this.mContainer.addView((View) this.mContent, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            this.mContent.addView(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            super.setContentView((View) this.mContainer);
        }
    }

    public void setBackgroundDrawable(@NotNull Drawable background) {
        this.mContent.setBackground(background);
    }

    public final void showAsPointer(@NotNull View anchor) {
        this.showAsPointer(anchor, 0, -20);
    }

    private void showAsPointer(View anchor, int xOffset, int yOffset) {
        int[] loc = new int[2];
        anchor.getLocationInWindow(loc);

        super.showAtLocation(anchor, Gravity.NO_GRAVITY, loc[0], loc[1]);

        dimBehind();
    }

    public void dimBehind() {
        View container;
        if (this.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) this.getContentView().getParent();
            } else {
                container = this.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) this.getContentView().getParent().getParent();
            } else {
                container = (View) this.getContentView().getParent();
            }
        }

        Context context = this.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

    public CustomPopupWindow(@NotNull Context context) {
        super(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.mContainer = new LinearLayout(context);
        this.mContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.mContainer.setOrientation(LinearLayout.HORIZONTAL);
        this.mContent = new FrameLayout(context);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
//
        this.setBackgroundDrawable(AppCompatResources.getDrawable(context, R.drawable.background_popup_style));
    }
}

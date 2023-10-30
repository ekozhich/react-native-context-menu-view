package com.mpiannucci.reactnativecontextmenu.popup;

import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPopupItem {
    @NotNull
    private String title;

    private boolean isSelected;

    private int order;

    private Drawable systemIcon;

    @NotNull
    public String getTitle() {
        return this.title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    public boolean getIsSelected() {
        return isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Nullable
    public Drawable getSystemIcon() {
        return systemIcon;
    }
    public void setSystemIcon(@Nullable Drawable systemIcon) {
        this.systemIcon = systemIcon;
    }
}

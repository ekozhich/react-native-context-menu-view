package com.mpiannucci.reactnativecontextmenu.extensions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import javax.annotation.Nullable;

public class DrawableExtension {
    public static Drawable getResourceWithName(Context context, @Nullable String systemIcon) {
        if (systemIcon == null)
            return null;

        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(systemIcon, "drawable", context.getPackageName());
        try {
            return resourceId != 0 ? ResourcesCompat.getDrawable(resources, resourceId, null) : null;
        } catch (Exception e) {
            return null;
        }
    }
}

package com.mpiannucci.reactnativecontextmenu.extensions;

import android.content.Context;

public class ColorExtension {
    public static int getColor(Context context, int colorResource) {
        return context.getResources().getColor(colorResource, context.getTheme());
    }
}

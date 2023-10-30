package com.mpiannucci.reactnativecontextmenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.mpiannucci.reactnativecontextmenu.extensions.ColorExtension;
import com.mpiannucci.reactnativecontextmenu.extensions.DrawableExtension;
import com.mpiannucci.reactnativecontextmenu.popup.CustomPopupAdapter;
import com.mpiannucci.reactnativecontextmenu.popup.CustomPopupItem;
import com.mpiannucci.reactnativecontextmenu.popup.CustomPopupItemOnClickInterface;
import com.mpiannucci.reactnativecontextmenu.popup.CustomPopupWindow;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ContextMenuView extends ReactViewGroup {
    CustomPopupWindow popupWindow;

    GestureDetector gestureDetector;

    protected boolean dropdownMenuMode = false;

    private ArrayList<CustomPopupItem> items = new ArrayList();

    public ContextMenuView(final Context context) {
        super(context);

        popupWindow = new CustomPopupWindow(getContext());

        popupWindow.setOnDismissListener(onDismissListener);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (dropdownMenuMode) {
                    show();
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (!dropdownMenuMode) {
                    show();
                }
            }
        });
    }

    public PopupWindow.OnDismissListener onDismissListener = () -> {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onCancel", null);
    };

    private void show() {
        CustomPopupAdapter adapter = new CustomPopupAdapter(getContext(), items);
        adapter.setOnClickListener(new CustomPopupItemOnClickInterface() {
            @Override
            public void onClickItem(CustomPopupItem customPopupItem) {
                onMenuItemClick(customPopupItem);
            }
        });

        ListView listView = new ListView(getContext());

        listView.setDivider(new ColorDrawable(ColorExtension.getColor(getContext(), R.color.popup_item_text_color)));
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);
        listView.setElevation(10);

        popupWindow.setContentView(listView);
        popupWindow.showAsPointer(this);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);

        child.setClickable(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return true;
    }

    public void onMenuItemClick(CustomPopupItem popupItem) {
        ReactContext reactContext = (ReactContext) getContext();
        WritableMap event = Arguments.createMap();
        event.putInt("index", popupItem.getOrder());
        event.putString("name", popupItem.getTitle());
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onPress", event);
        popupWindow.dismiss();
    }

    public void setActions(@Nullable ReadableArray actions) {
        items.clear();

        for (int i = 0; i < actions.size(); i++) {
            ReadableMap action = actions.getMap(i);

            boolean isSelected = action.hasKey("selected") && action.getBoolean("selected");
            @Nullable Drawable systemIcon = DrawableExtension.getResourceWithName(getContext(), action.getString("systemIcon"));
            String title = action.getString("title");

            CustomPopupItem customPopupItem = new CustomPopupItem();
            customPopupItem.setTitle(title);
            customPopupItem.setOrder(i);
            customPopupItem.setIsSelected(isSelected);
            customPopupItem.setSystemIcon(systemIcon);

            items.add(customPopupItem);
        }
    }

    public void setDropdownMenuMode(@Nullable boolean enabled) {
        this.dropdownMenuMode = enabled;
    }
}

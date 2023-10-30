package com.mpiannucci.reactnativecontextmenu.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.mpiannucci.reactnativecontextmenu.R;
import com.mpiannucci.reactnativecontextmenu.R.id;
import com.mpiannucci.reactnativecontextmenu.R.layout;
import com.mpiannucci.reactnativecontextmenu.extensions.ColorExtension;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CustomPopupAdapter extends ArrayAdapter {
    private ArrayList<CustomPopupItem> usersModels;

    public int getCount() {
        return this.usersModels.size();
    }

    private CustomPopupItemOnClickInterface customPopupItemOnClickInterface;

    @NotNull
    public View getView(int position, @Nullable View convertView, @NotNull ViewGroup parent) {
        View view = LayoutInflater.from(this.getContext()).inflate(layout.item, parent, false);
        PopupTitleViewHolder holder = new PopupTitleViewHolder(view);
        CustomPopupItem customPopupItem = this.usersModels.get(position);
        holder.bind(customPopupItem);
        return view;
    }

    public CustomPopupAdapter(@NotNull Context context, @NotNull ArrayList<CustomPopupItem> usersModels) {
        super(context, 0, usersModels);
        this.usersModels = usersModels;
    }

    public void setOnClickListener(CustomPopupItemOnClickInterface customPopupItemOnClickInterface) {
        this.customPopupItemOnClickInterface = customPopupItemOnClickInterface;
    }

    public class PopupTitleViewHolder {
        private View view;

        private Drawable getBackground(int order) {
            if (order == 0) {
                return AppCompatResources.getDrawable(getContext(), R.drawable.background_popup_style_active_first);
            }

            if (order == getCount() - 1) {
                return AppCompatResources.getDrawable(getContext(), R.drawable.background_popup_style_active_last);
            }

            return AppCompatResources.getDrawable(getContext(), R.drawable.background_popup_style_active_middle);
        }

        public void bind(@NotNull CustomPopupItem customPopupItem) {
            String title = customPopupItem.getTitle();
            int order = customPopupItem.getOrder();
            boolean isSelected = customPopupItem.getIsSelected();
            @Nullable Drawable systemIcon = customPopupItem.getSystemIcon();

            TextView textView = this.view.findViewById(id.customPopupItemTitle);
            textView.setText(title);

           try {
               Typeface font = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.popup_item_text_typeface_path));

               if (font != null) {
                   textView.setTypeface(font);
               }
           } catch (Exception e) {}

            if (systemIcon != null) {
                ImageView imageView = this.view.findViewById(id.customPopupItemIcon);
                imageView.setImageDrawable(systemIcon);
            }

            view.setOnClickListener(view -> {
                customPopupItemOnClickInterface.onClickItem(customPopupItem);
            });

            if (isSelected) {
                view.setBackground(getBackground(order));
                textView.setTextColor(ColorExtension.getColor(getContext(), R.color.popup_item_active_text_color));
            }
        }

        public PopupTitleViewHolder(@NotNull View view) {
            this.view = view;
        }
    }
}

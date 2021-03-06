/*
 *
 *  *    Copyright 2018. iota9star
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.afollestad.aesthetic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.lang.reflect.Field;

import io.reactivex.Observable;

import static com.afollestad.aesthetic.Util.isColorLight;
import static com.afollestad.aesthetic.Util.resolveResId;

/**
 * @author Aidan Follestad (afollestad)
 */
public final class ViewUtil {

    @Nullable
    public static Observable<Integer> getObservableForResId(@NonNull Context context, @IdRes int resId, @Nullable Observable<Integer> fallback) {
        if (resId == 0) {
            return fallback;
        } else if (resId == resolveResId(context, R.attr.colorPrimary, 0)) {
            return Aesthetic.get().colorPrimary();
        } else if (resId == resolveResId(context, R.attr.colorPrimaryDark, 0)) {
            return Aesthetic.get().colorPrimaryDark();
        } else if (resId == resolveResId(context, android.R.attr.statusBarColor, 0)) {
            return Aesthetic.get().colorStatusBar();
        } else if (resId == resolveResId(context, R.attr.colorAccent, 0)) {
            return Aesthetic.get().colorAccent();
        } else if (resId == resolveResId(context, android.R.attr.windowBackground, 0)) {
            return Aesthetic.get().colorWindowBackground();
        } else if (resId == resolveResId(context, android.R.attr.textColorPrimary, 0)) {
            return Aesthetic.get().textColorPrimary();
        } else if (resId == resolveResId(context, android.R.attr.textColorPrimaryInverse, 0)) {
            return Aesthetic.get().textColorPrimaryInverse();
        } else if (resId == resolveResId(context, android.R.attr.textColorSecondary, 0)) {
            return Aesthetic.get().textColorSecondary();
        } else if (resId == resolveResId(context, android.R.attr.textColorSecondaryInverse, 0)) {
            return Aesthetic.get().textColorSecondaryInverse();
        }
        return fallback;
    }

    static void tintToolbarMenu(@NonNull Toolbar toolbar, @NonNull Menu menu, ActiveInactiveColors titleIconColors) {
        // The collapse icon displays when action views are expanded (e.g. SearchView)
        try {
            final Field field = Toolbar.class.getDeclaredField("mCollapseIcon");
            field.setAccessible(true);
            Drawable collapseIcon = (Drawable) field.get(toolbar);
            if (collapseIcon != null) {
                field.set(toolbar, TintHelper.createTintedDrawable(collapseIcon, titleIconColors.toEnabledSl()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Theme menu action views
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getIcon() != null) {
                Drawable tintedDrawable = TintHelper.createTintedDrawable(item.getIcon(), titleIconColors.toEnabledSl());
                item.setIcon(tintedDrawable);
            }
            if (item.getActionView() instanceof SearchView) {
                themeSearchView(titleIconColors, (SearchView) item.getActionView());
            }
        }
    }

    private static void themeSearchView(ActiveInactiveColors tintColors, SearchView view) {
        final Class<?> cls = view.getClass();
        try {
            final Field mSearchSrcTextViewField = cls.getDeclaredField("mSearchSrcTextView");
            mSearchSrcTextViewField.setAccessible(true);
            final EditText mSearchSrcTextView = (EditText) mSearchSrcTextViewField.get(view);
            mSearchSrcTextView.setTextColor(tintColors.activeColor());
            mSearchSrcTextView.setHintTextColor(tintColors.inactiveColor());
            TintHelper.setCursorTint(mSearchSrcTextView, tintColors.activeColor());

            Field field = cls.getDeclaredField("mSearchButton");
            tintImageView(view, field, tintColors);
            field = cls.getDeclaredField("mGoButton");
            tintImageView(view, field, tintColors);
            field = cls.getDeclaredField("mCloseButton");
            tintImageView(view, field, tintColors);
            field = cls.getDeclaredField("mVoiceButton");
            tintImageView(view, field, tintColors);

            field = cls.getDeclaredField("mSearchPlate");
            field.setAccessible(true);
            TintHelper.setTintAuto(
                    (View) field.get(view),
                    tintColors.activeColor(),
                    true,
                    !isColorLight(tintColors.activeColor()));

            field = cls.getDeclaredField("mSearchHintIcon");
            field.setAccessible(true);
            field.set(view, TintHelper.createTintedDrawable((Drawable) field.get(view), tintColors.toEnabledSl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tintImageView(Object target, Field field, ActiveInactiveColors tintColors) throws Exception {
        field.setAccessible(true);
        final ImageView imageView = (ImageView) field.get(target);
        if (imageView.getDrawable() != null) {
            imageView.setImageDrawable(TintHelper.createTintedDrawable(imageView.getDrawable(), tintColors.toEnabledSl()));
        }
    }
}

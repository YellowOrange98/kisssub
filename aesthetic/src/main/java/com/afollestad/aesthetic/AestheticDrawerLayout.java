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
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.AttributeSet;

import io.reactivex.disposables.Disposable;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

/**
 * @author Aidan Follestad (afollestad)
 */
public class AestheticDrawerLayout extends DrawerLayout {

    private ActiveInactiveColors lastState;
    private DrawerArrowDrawable arrowDrawable;
    private Disposable subscription;

    public AestheticDrawerLayout(Context context) {
        super(context);
    }

    public AestheticDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AestheticDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void invalidateColor(ActiveInactiveColors colors) {
        if (colors == null) {
            return;
        }
        this.lastState = colors;
        if (this.arrowDrawable != null) {
            this.arrowDrawable.setColor(lastState.activeColor());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        subscription = Aesthetic.get()
                .colorIconTitle(null)
                .compose(Rx.distinctToMainThread())
                .subscribe(this::invalidateColor, onErrorLogAndRethrow());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (subscription != null) {
            subscription.dispose();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void addDrawerListener(@NonNull DrawerListener listener) {
        super.addDrawerListener(listener);
        if (listener instanceof ActionBarDrawerToggle) {
            this.arrowDrawable = ((ActionBarDrawerToggle) listener).getDrawerArrowDrawable();
        }
        invalidateColor(lastState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setDrawerListener(DrawerListener listener) {
        super.setDrawerListener(listener);
        if (listener instanceof ActionBarDrawerToggle) {
            this.arrowDrawable = ((ActionBarDrawerToggle) listener).getDrawerArrowDrawable();
        }
        invalidateColor(lastState);
    }
}

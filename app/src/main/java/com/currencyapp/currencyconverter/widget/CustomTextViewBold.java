package com.currencyapp.currencyconverter.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by raghav on 30/1/16.
 */
public class CustomTextViewBold extends TextView {
    public CustomTextViewBold(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font =Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(font, Typeface.BOLD);
    }
}
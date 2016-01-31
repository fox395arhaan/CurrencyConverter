package com.currencyapp.currencyconverter.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by raghav on 30/1/16.
 */
public class CustomEditTextView extends EditText {
    public CustomEditTextView(Context context) {
        super(context);
        setFont();
    }

    public CustomEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font =Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(font, Typeface.BOLD);
    }
}
package com.currencyapp.currencyconverter.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by raghav on 30/1/16.
 */
public class AppCustomTextView extends AppCompatTextView{

    public AppCustomTextView(Context context) {
        super(context);
        setFont();
    }

    public AppCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public AppCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font =Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(font, Typeface.BOLD);
    }
}

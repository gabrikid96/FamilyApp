package grodrich.grc.familyapp.view.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import grodrich.grc.familyapp.R;

/**
 * Created by gabri on 27/08/16.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        aplicateFont(getFontPath(attrs));
    }

    private String getFontPath(AttributeSet attrs){
        TypedArray theAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String font = theAttrs.getString(R.styleable.CustomTextView_font);
        theAttrs.recycle();
        String defaultFont = "fonts/Canter Light.otf";
        if (font == null){
            font = defaultFont;
        }else{
            font = "fonts/".concat(font);
        }
        return font;
    }

    private void aplicateFont(String fontPath){
        Typeface TF = Typeface.createFromAsset(getContext().getAssets(),fontPath);
        this.setTypeface(TF);
    }
}

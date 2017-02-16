package lorentzonsolutions.rhome.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import lorentzonsolutions.rhome.utils.FontCreator;

/**
 * A custom view using a custom font.
 */

public class HeaderTextView extends TextView {


    public HeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public HeaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Typeface tf = FontCreator.create(getContext(), FontCreator.TypefaceName.EL_TERCER_HOMBRE);
        setTypeface(tf);
    }

}

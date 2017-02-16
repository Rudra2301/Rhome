package lorentzonsolutions.rhome.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import lorentzonsolutions.rhome.utils.FontCreator;

/**
 * Custom button using custom typeface.
 */

public class HombreButton extends Button {
    public HombreButton(Context context) {
        super(context);
        init();
    }

    public HombreButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Typeface tf = FontCreator.create(getContext(), FontCreator.TypefaceName.EL_TERCER_HOMBRE);
        setTypeface(tf);
    }
}

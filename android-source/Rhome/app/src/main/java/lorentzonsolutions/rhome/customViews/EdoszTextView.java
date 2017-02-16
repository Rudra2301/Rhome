package lorentzonsolutions.rhome.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import lorentzonsolutions.rhome.utils.FontCreator;

/**
 * Created by johanlorentzon on 2017-02-16.
 */

public class EdoszTextView extends TextView {
    public EdoszTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EdoszTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = FontCreator.create(getContext(), FontCreator.TypefaceName.EDOSZ);
        setTypeface(tf);
    }
}

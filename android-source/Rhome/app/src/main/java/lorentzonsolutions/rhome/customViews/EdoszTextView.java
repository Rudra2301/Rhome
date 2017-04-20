package lorentzonsolutions.rhome.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import lorentzonsolutions.rhome.utils.FontCreator;

public class EdoszTextView extends android.support.v7.widget.AppCompatTextView {
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

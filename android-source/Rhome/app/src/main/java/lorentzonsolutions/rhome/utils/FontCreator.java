package lorentzonsolutions.rhome.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * This class is for creating typefonts.
 */

public class FontCreator {

    public static Typeface create(Context context, TypefaceName name) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), name.typeface);
        return tf;
    }

    public enum TypefaceName {
        EL_TERCER_HOMBRE("fonts/eltercerhombre.ttf"), EDOSZ("fonts/edosz.ttf");

        String typeface;

        TypefaceName(String name) {
            this.typeface = name;
        }
    }
}

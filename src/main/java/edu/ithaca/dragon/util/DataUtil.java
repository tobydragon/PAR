package edu.ithaca.dragon.util;

import java.text.DecimalFormat;

/**
 * Created by tdragon on 5/3/17.
 */
public class DataUtil {
    public static double OK_DOUBLE_MARGIN = (double) 0.00001;

    public static boolean equalsDoubles(double one, double two) {
        return (Math.abs(one - two) < OK_DOUBLE_MARGIN);
    }

    public static String format(double toFormat) {
        DecimalFormat df = new DecimalFormat("####0.000");
        return df.format(toFormat);
    }

    public static boolean objectEqualsIncludingNull(Object thing1, Object thing2){
        if (thing1 == null){
            return thing2 == null;
        }
        else {
            return thing1.equals(thing2);
        }
    }
}

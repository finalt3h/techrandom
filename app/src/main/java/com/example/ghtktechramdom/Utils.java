package com.example.ghtktechramdom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isValidContextForExecute(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public static boolean isRtl(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static int darker(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.max((int) (Color.red(color) * factor), 0),
                Math.max((int) (Color.green(color) * factor), 0), Math.max((int) (Color.blue(color) * factor), 0));
    }

    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        }
        return context.getResources().getDrawable(id);
    }



    public static String prettyCount(long numValue) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }
    public static boolean isNumericPhone(String str) {
        String phone = convertPhoneNumberV2(str);
        try {
            Double.parseDouble(phone);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static final Pattern PATTERN_PHONE = Patterns.PHONE;
    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || target.length() < 9 || target.length() > 12) {
            return false;
        } else {
            return PATTERN_PHONE.matcher(target).matches();
        }

    }




    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }
    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
    public static String convertPhoneNumberV2(String phone) {
        String type2 = "\\+84";
        Pattern patternType2 = Pattern.compile("^" + type2);
        Matcher matcherType2 = patternType2.matcher(phone);
        if (matcherType2.find()) {
            String resultType2 = "0";
            phone = matcherType2.replaceFirst(resultType2);
        }

        String type3 = "84";
        Pattern patternType3 = Pattern.compile("^" + type3);
        Matcher matcherType3 = patternType3.matcher(phone);
        if (matcherType3.find()) {
            String resultType3 = "0";
            phone = matcherType3.replaceFirst(resultType3);

        }

        return phone;
    }

    public static boolean isValidContext(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return !activity.isDestroyed() && !activity.isFinishing();
            } else {
                return !activity.isFinishing();
            }
        }
        return true;
    }


    public static Long parseLong(String value) {
        String resultString;
        if (value.contains(".")) {
            int indexOf = value.indexOf(".");

            resultString = value.substring(0, indexOf);
        } else {
            resultString = value;
        }

        try {
            BigInteger result = new BigInteger(resultString);
            return result.longValue();
        } catch (Exception e) {
            return (long) 0;
        }
    }
    private final static int UPPER_LEFT_X = 0;
    private final static int UPPER_LEFT_Y = 0;
    public static Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(UPPER_LEFT_X, UPPER_LEFT_Y, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }
}

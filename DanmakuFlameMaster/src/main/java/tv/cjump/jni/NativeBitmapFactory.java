
package tv.cjump.jni;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.lang.reflect.Field;

public class NativeBitmapFactory {

    private static Field nativeIntField = null;

    private static boolean nativeLibLoaded = false;
    private static boolean notLoadAgain = false;
    
    static boolean isInNativeAlloc() {
        return nativeLibLoaded && nativeIntField != null;
    }

    public static void loadLibs() {
        if (notLoadAgain) {
            return;
        }
        notLoadAgain = true;
        nativeLibLoaded = false;

        Log.v("NativeBitmapFactory", "will not load ndkbitmap");
    }

    public static synchronized void releaseLibs() {
        boolean loaded =  nativeLibLoaded;
        nativeIntField = null;
        nativeLibLoaded = false;
        if (loaded) {
            release();
        }
        // Log.e("NativeBitmapFactory", "released");
    }

    private static void initField() {
        try {
            nativeIntField = Bitmap.Config.class.getDeclaredField("nativeInt");
            nativeIntField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            nativeIntField = null;
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private static boolean testLib() {
        if (nativeIntField == null) {
            return false;
        }
        Bitmap bitmap = null;
        Canvas canvas;
        try {
            bitmap = createNativeBitmap(2, 2, Bitmap.Config.ARGB_8888, true);
            boolean result = (bitmap != null && bitmap.getWidth() == 2 && bitmap.getHeight() == 2);
            if (result) {
                if (android.os.Build.VERSION.SDK_INT >= 17 && !bitmap.isPremultiplied()) {
                    bitmap.setPremultiplied(true);
                }
                canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(20f);
                canvas.drawRect(0f, 0f, (float) bitmap.getWidth(), (float) bitmap.getHeight(),
                        paint);
                canvas.drawText("TestLib", 0, 0, paint);
                if (android.os.Build.VERSION.SDK_INT >= 17) {
                    result = bitmap.isPremultiplied();
                }
            }
            return result;
        } catch (Exception e) {
            Log.e("NativeBitmapFactory", "exception:" + e.toString());
            return false;
        } catch (Error e) {
            return false;
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    private static int getNativeConfig(Bitmap.Config config) {
        try {
            if (nativeIntField == null) {
                return 0;
            }
            return nativeIntField.getInt(config);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        return createBitmap(width, height, config, config.equals(Bitmap.Config.ARGB_4444) || config.equals(Bitmap.Config.ARGB_8888));
    }

    public static void recycle(Bitmap bitmap) {
        bitmap.recycle();
    }

    private static synchronized Bitmap createBitmap(int width, int height, Bitmap.Config config, boolean hasAlpha) {
        if (!nativeLibLoaded || nativeIntField == null) {
            // Log.e("NativeBitmapFactory", "ndk bitmap create failed");
            return Bitmap.createBitmap(width, height, config);
        }
        return createNativeBitmap(width, height, config, hasAlpha);
    }

    private static Bitmap createNativeBitmap(int width, int height, Config config, boolean hasAlpha) {
        int nativeConfig = getNativeConfig(config);
        // Log.e("NativeBitmapFactory", "nativeConfig:" + nativeConfig);
        // Log.e("NativeBitmapFactory", "create bitmap:" + bitmap);
        return android.os.Build.VERSION.SDK_INT == 19 ? createBitmap19(width, height,
                nativeConfig, hasAlpha) : createBitmap(width, height, nativeConfig, hasAlpha);
    }

    // ///////////native methods//////////

    private static native boolean init();

    private static native boolean release();

    private static native Bitmap createBitmap(int width, int height, int nativeConfig,
            boolean hasAlpha);

    private static native Bitmap createBitmap19(int width, int height, int nativeConfig,
            boolean hasAlpha);

}

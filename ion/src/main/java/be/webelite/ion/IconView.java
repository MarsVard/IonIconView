package be.webelite.ion;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Aper on 15/01/14.
 */
class TypeFaceWrapper {
    private static Typeface ionfont;

    static Typeface getTypeface() {
        return ionfont;
    }

    static void setParameter(Typeface ionfont) {
        TypeFaceWrapper.ionfont = ionfont;
    }
}
public class IconView extends TextView {

    public static int ROT_LEFT_NORMAL = 0;
    public static int ROT_LEFT_STEPS = 1;

    protected final String icons_rot_left_1[] = new String[]{
            "ion_refreshing", "ion_looping", "ion_loading_b",
            "ion_loading_c", "ion_loading_d", "ion_ios7_reloading"
    };

    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    protected void init(AttributeSet attrs){
        // set correct unicode character as text
        TypedArray a                            = getContext().obtainStyledAttributes(attrs, be.webelite.ion.R.styleable.icon);
        String icon_name                        = a.getString(be.webelite.ion.R.styleable.icon_name);

        if(icon_name != null){
            Icon icon                        = Icon.valueOf(icon_name);
            this.setIcon(icon);
        }else{
            return;
        }
        a.recycle();

        if(!isInEditMode()){

            // read typeface only once!
            // yey performance :)
            if(TypeFaceWrapper.getTypeface() == null){
                TypeFaceWrapper.setParameter(this.getFontFromRes(R.raw.ionicons, getContext()));
            }

            this.setTypeface(TypeFaceWrapper.getTypeface());
        }

    }

    public void setIcon(Icon icon){
        this.setText( Character.toString( (char) icon.unicode_value) );

        if(Arrays.asList(icons_rot_left_1).contains(icon.toString())){
            this.setAnimation(IconView.ROT_LEFT_NORMAL);
        }else{
            this.clearAnimation();
        }
    }

    public static Typeface getFontFromRes(int resource, Context context)
    {
        Typeface tf = null;
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(resource);
        }
        catch(Resources.NotFoundException e) {
            Log.e("getFontFromRes", "Could not find font in resources!");
        }

        String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

        try
        {
            byte[] buffer = new byte[is.available()];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

            int l = 0;
            while((l = is.read(buffer)) > 0)
                bos.write(buffer, 0, l);

            bos.close();

            tf = Typeface.createFromFile(outPath);

            // clean up
            new File(outPath).delete();
        }
        catch (IOException e)
        {
            Log.e("getFontFromRes", "Error reading in font!");
            return null;
        }

        Log.d("getFontFromRes", "Successfully loaded font.");

        return tf;
    }

    public void setAnimation(int animation){
        switch(animation){
            case 0: // ROT_LEFT_NORMAL
                this.startAnimation( AnimationUtils.loadAnimation(getContext(), R.anim.rotate_left_1) );
                break;
            case 1: // ROT_LEFT_STEPS TODO
                this.startAnimation( AnimationUtils.loadAnimation(getContext(), R.anim.rotate_left_1) );
                break;
        }
    }
    public void stopAnimation(){
        this.clearAnimation();
    }
}

package com.rapdict.takuro.rapdict;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WidgetController {
    //TextViewのデザインを決める
    Context context;
    public WidgetController(Context context){
        this.context=context;
    }
    
    public TextView settings(TextView textView, float padding_right, float padding_left, float padding_bottom, float padding_top, float textsize, String text, int gravity, int width){
        textView.setPadding(int_Dp2Px(padding_left, context), int_Dp2Px(padding_top, context), int_Dp2Px(padding_right, context), int_Dp2Px(padding_bottom, context));
        textView.setTextSize(Dp2Px(textsize,context));
        textView.setWidth(width);
        textView.setGravity(gravity);
        textView.setText(text);
        return textView;
    }
    //EditTextのデザインを決める。
    public EditText settings(EditText editText, float padding_right, float padding_left, float padding_bottom, float padding_top, float textsize, int gravity, int width){
        editText.setPadding(int_Dp2Px(padding_left, context), int_Dp2Px(padding_top, context), int_Dp2Px(padding_right, context), int_Dp2Px(padding_bottom, context));
        editText.setTextSize(Dp2Px(textsize,context));
        editText.setGravity(gravity);
        editText.setWidth(width);
        return editText;
    }
    //Buttonのデザインを決める
    public Button settings(Button button, float padding_right, float padding_left, float padding_bottom, float padding_top, float textsize, String text, int gravity, int width){
        button.setPadding(int_Dp2Px(padding_left, context), int_Dp2Px(padding_top, context), int_Dp2Px(padding_right, context), int_Dp2Px(padding_bottom, context));
        button.setTextSize(Dp2Px(textsize,context));
        button.setWidth(width);
        button.setGravity(gravity);
        button.setText(text);
        return button;
    }
    //Dpをピクセルに変換する関数
    public static float Dp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }
    //Dpをピクセルに変換、int型に変換する。
    public static int int_Dp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float n=dp * metrics.density;
        return (int)n;
    }
}

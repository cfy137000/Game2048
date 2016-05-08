package com.lanou.chenfengyao.game2048.myview;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ChenFengYao on 16/5/7.
 */
public class M2048Item extends FrameLayout {
    private TextView textView;
    private int number;

    public M2048Item(Context context) {
        super(context);
        this.number = 0;
        textView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        textView.setText("");
        textView.setGravity(Gravity.CENTER);
        setColor(0);
        layoutParams.setMargins(5,5,5,5);
        addView(textView,layoutParams);
    }


    public void setNumber(int number) {
        this.number = number;
        textView.setText(String.valueOf(number));
        setColor(number);
    }

    public int getNumber() {
        return number;
    }

    private void setColor(int num) {
        int mBgColor;
        switch (num) {
            case 0:
                mBgColor = 0xFFCCC0B3;
                break;
            case 2:
                mBgColor = 0xFFEEE4DA;
                break;
            case 4:
                mBgColor = 0xFFEDE0C8;
                break;
            case 8:
                mBgColor = 0xFFF2B179;// 0xFFF2B179
                break;
            case 16:
                mBgColor = 0xFFF49563;
                break;
            case 32:
                mBgColor = 0xFFF5794D;
                break;
            case 64:
                mBgColor = 0xFFF55D37;
                break;
            case 128:
                mBgColor = 0xFFEEE863;
                break;
            case 256:
                mBgColor = 0xFFEDB04D;
                break;
            case 512:
                mBgColor = 0xFFECB04D;
                break;
            case 1024:
                mBgColor = 0xFFEB9437;
                break;
            case 2048:
                mBgColor = 0xFFEA7821;
                break;
            default:
                mBgColor = 0xFFEA7821;
                break;
        }
        textView.setBackgroundColor(mBgColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                Toast.makeText(getContext(), "移动", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onTouchEvent(event);
    }
}

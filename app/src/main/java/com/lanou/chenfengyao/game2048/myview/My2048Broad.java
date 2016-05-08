package com.lanou.chenfengyao.game2048.myview;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by ChenFengYao on 16/5/7.
 */
public class My2048Broad extends GridLayout {
    int lineNumber = 4;
    private int length;
    private int[][] numbers;//记录各个位置的数字
    private List<Point> blankPoint;
    private M2048Item[][] m2048Items;
    private Random random;

    /**
     * 检测用户滑动的手势
     */
    private GestureDetector mGestureDetector;


    public My2048Broad(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRowCount(lineNumber);
        setColumnCount(lineNumber);
        setBackgroundColor(Color.BLUE);
        setPadding(0, 0, 0, 0);

    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(length, length);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        initBroad();
    }

    //初始化布局
    private void initBroad() {
        mGestureDetector = new GestureDetector(getContext(),new MyOnGestureListener());

        blankPoint = new LinkedList<>();
        random = new Random();
        numbers = new int[lineNumber][lineNumber];
        m2048Items = new M2048Item[lineNumber][lineNumber];
        for (int i = 0; i < lineNumber; i++) {
            for (int j = 0; j < lineNumber; j++) {
                M2048Item m2048Item = new M2048Item(getContext());
                m2048Items[i][j] = m2048Item;
                addView(m2048Item,
                        length / 4, length / 4);
                blankPoint.add(new Point(i, j));
                numbers[i][j] = 0;
            }
        }

        //第一次生成2个随机数
        getNewRandomNum();
        getNewRandomNum();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    //生成一个随机的方块
    public void getNewRandomNum() {
        int randomNum = random.nextInt(blankPoint.size());
        int row, column;
        row = blankPoint.get(randomNum).x;
        column = blankPoint.get(randomNum).y;
        int nextNum = random.nextFloat() > 0.75 ? 4 : 2;
        numbers[row][column] = nextNum;
        m2048Items[row][column].setNumber(nextNum);
        blankPoint.remove(randomNum);
    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        final int FLING_MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float dx = e2.getX() - e1.getX();
            float dy = e2.getY() - e1.getY();

            if (dx > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY)) {
                Log.d("MyOnGestureListener", "right");

            } else if (dx < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY)) {
                Log.d("MyOnGestureListener", "left");

            } else if (dy > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY)) {
                Log.d("MyOnGestureListener", "down");

            } else if (dy < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY)) {
                Log.d("MyOnGestureListener", "up");
            }
            return true;

        }

    }
}

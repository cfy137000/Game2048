package com.lanou.chenfengyao.game2048.myview;

import android.animation.ObjectAnimator;
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
    private boolean once = true;


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
        if (once) {

            initBroad();
            once = false;
        }
    }

    //初始化布局
    private void initBroad() {
        mGestureDetector = new GestureDetector(getContext(), new MyOnGestureListener());

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
        getAllBlank();
        int randomNum = random.nextInt(blankPoint.size());
        int row, column;
        row = blankPoint.get(randomNum).x;
        column = blankPoint.get(randomNum).y;
        int nextNum = random.nextFloat() > 0.75 ? 4 : 2;
        numbers[row][column] = nextNum;
        setNewBroad(row, column);
    }

    //生成一个带动画的
    private void setNewBroad(int row, int column) {
        Log.d("My2048Broad", "带动画");
        M2048Item m2048Item = ((M2048Item) getChildAt(row * lineNumber + column));
        m2048Item.setNumber(numbers[row][column]);
        ObjectAnimator.ofFloat(m2048Item, "scaleX"
                , 0, 1).start();
        ObjectAnimator.ofFloat(m2048Item, "scaleY"
                , 0, 1).start();
    }

    public void getAllBlank() {
        blankPoint.clear();
        for (int i = 0; i < lineNumber; i++) {
            for (int j = 0; j < lineNumber; j++) {
                if (numbers[i][j] == 0) {
                    blankPoint.add(new Point(i, j));
                }
            }
        }
    }

    private void moveLeft() {
        //如果是向右看每一行的最右边
        for (int i = 0; i < lineNumber; i++) {
            List<Integer> number = new ArrayList();
            for (int j = 0; j < lineNumber; j++) {
                if (numbers[i][j] != 0) {
                    number.add(numbers[i][j]);
                }
            }
            //融合临近相同的
            mergeNumber(number);
            // 设置合并后的值
            for (int j = 0; j < lineNumber; j++) {
                //  int index = getIndexByAction(action, i, j);
                if (number.size() > j) {
                    numbers[i][j] = number.get(j);
                } else {
                    numbers[i][j] = 0;
                }
            }
        }
        setNewBroad();
    }

    public void mergeNumber(List<Integer> number) {
        if (number.size() < 2)
            return;
        for (int j = 0; j < number.size() - 1; j++) {
            int item1 = number.get(j);
            int item2 = number.get(j + 1);

            if (item1 == item2) {

                int val = item1 + item2;
                number.set(j, val);
                // 加分
                // 向前移动
                for (int k = j + 1; k < number.size() - 1; k++) {
                    number.set(k, number.get(k + 1));
                }

                number.remove(number.size() - 1);
                return;
            }

        }
    }


    public void setNewBroad() {
        for (int i = 0; i < lineNumber; i++) {
            for (int j = 0; j < lineNumber; j++) {
                ((M2048Item) getChildAt(i * lineNumber + j)).setNumber(numbers[i][j]);
            }
        }
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
                moveRight();

            } else if (dx < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY)) {
                moveLeft();

            } else if (dy > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY)) {
                moveDown();

            } else if (dy < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY)) {
                moveUp();
            }
            //移动完成来个新的数字

            getNewRandomNum();

            return true;

        }

    }

    private void moveDown() {
        //如果是向右看每一行的最右边
        for (int j = 0; j < lineNumber; j++) {
            List<Integer> number = new ArrayList();
            for (int i = 0; i < lineNumber; i++) {
                if (numbers[i][j] != 0) {
                    number.add(numbers[i][j]);
                }
            }
            //融合临近相同的
            mergeNumber(number);
            //调换一次顺序
            number = turnNumber(number);
            // 设置合并后的值
            int k = lineNumber - 1;
            for (int i = number.size() - 1; i >= 0; i--) {
                numbers[k][j] = number.get(i);
                k--;
            }
            while (k >= 0) {
                numbers[k][j] = 0;
                k--;
            }
        }
        setNewBroad();
    }

    //向上移动
    private void moveUp() {
        //如果是向右看每一行的最右边
        for (int j = 0; j < lineNumber; j++) {
            List<Integer> number = new ArrayList();
            for (int i = 0; i < lineNumber; i++) {
                if (numbers[i][j] != 0) {
                    number.add(numbers[i][j]);
                }
            }
            //融合临近相同的
            mergeNumber(number);
            //调换一次顺序
            //number = turnNumber(number);
            // 设置合并后的值
            // 设置合并后的值
            // 设置合并后的值
            int k = 0;
            for (int i = 0; i < number.size(); i++) {
                numbers[k][j] = number.get(i);
                k++;
            }
            while (k < lineNumber) {
                numbers[k][j] = 0;
                k++;
            }
        }
        setNewBroad();
    }

    //向右移动
    private void moveRight() {
        //如果是向右看每一行的最右边
        for (int i = 0; i < lineNumber; i++) {
            List<Integer> number = new ArrayList();
            for (int j = 0; j < lineNumber; j++) {
                if (numbers[i][j] != 0) {
                    number.add(numbers[i][j]);
                }
            }
            //融合临近相同的
            mergeNumber(number);
            //调换一次顺序
            // 设置合并后的值
            int k = lineNumber - 1;
            for (int j = number.size() - 1; j >= 0; j--) {
                numbers[i][k] = number.get(j);
                k--;
            }
            while (k >= 0) {
                numbers[i][k] = 0;
                k--;
            }
        }
        setNewBroad();
    }

    //倒置list
    private List<Integer> turnNumber(List<Integer> numList) {
        List<Integer> afterTurnList = new ArrayList<>();
        for (Integer integer : numList) {
            if (integer != 0) {
                afterTurnList.add(integer);
            } else {
                afterTurnList.add(0, integer);
            }
        }
        return afterTurnList;
    }

}

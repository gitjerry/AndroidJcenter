package com.bench.viewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.view.View.MeasureSpec.getMode;

/**
 * @FileName:OrderStateView.java
 * @date:2018/05/18
 * @email: libenqi@pptv.com
 * @description:
 * @copyright 2018. LongZhu Inc. All rights reserved
 */

public class OrderStateView extends View
{
    private Bitmap mBitmapDone;
    private Bitmap mBitmapToConfirm;
    private Bitmap mBitmapUnDone;

    private Paint mBitmapPaint;
    private Paint mLinePaint;
    private Paint mOrderTimePaint;
    private int mViewW;
    private int mViewH;
    private String[] mStrings = new String[]{"已发约", "待确认", "待完成", "待评价"};
    private int mPointDistance;
    private String mOrderTime = "23:59:00";
    private Rect mBitmapRect;
    private int mBitmapY;

    private int mMarginLeft = dip2px(20);
    private int mMarginRight = dip2px(20);
    //间隙
    private int mGapLength = dip2px(5);
    private int mGapH = dip2px(5);
    //线长度
    private int mLineLength;
    private int mLineY;
    private int mLineH = dip2px(2);

    private int mStartX;
    private int mStopX;


    private TextPaint mTextPaint;

    private OrderState mOrderType = OrderState.waitConfirm;


    public OrderStateView(Context context) {
        this(context, null);
    }


    public OrderStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmapDone = BitmapFactory.decodeResource(getResources(), R.drawable.ic_orders_done);
//        mBitmapDone = BitmapFactory.decodeResource(getResources(), R.drawable.ic_orders_done);
        mBitmapToConfirm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_to_be_confirmed);
        mBitmapUnDone = BitmapFactory.decodeResource(getResources(), R.drawable.ic_undone);
        mBitmapRect = new Rect(0, 0, mBitmapDone.getWidth(), mBitmapDone.getHeight());
        Log.e("bench", "-mBitmapDone.width()->" + mBitmapDone.getWidth());
        Log.e("bench", "-mBitmapDone.getHeight()->" + mBitmapDone.getHeight());
        mBitmapPaint = new Paint();

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mLineH);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStyle(Paint.Style.STROKE);


        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(sp2px(12));

        mOrderTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOrderTimePaint.setColor(getResources().getColor(R.color.color_4D000000));
        mOrderTimePaint.setTextSize(sp2px(12));


    }

    public void setOrderState(OrderState cOrderState) {
        this.mOrderType=cOrderState;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mViewW = getMeasuredWidth();
        mViewH = getMeasuredHeight();
        mLineLength = (mViewW - mMarginLeft - mMarginRight - 4 * mBitmapRect.width() - 6 * mGapLength) / 3;
        mBitmapY = mViewH / 2 - mBitmapRect.height() / 2;
        mLineY = mViewH / 2 + mLineH / 2;
        mPointDistance = 2 * mGapLength + mLineLength + mBitmapRect.width();
        mStartX = mMarginLeft + mGapLength + mBitmapRect.width();
        mStopX = mStartX + mLineLength;
        drawOrderStyle(canvas, mOrderType);
    }

    private void drawOrderStyle(Canvas canvas, OrderState type) {
        switch (type) {
            case waitConfirm:
                drawToConfirmStyle(canvas);
                break;
            case waitFinish:
                drawToFinishStyle(canvas);
                break;
            case waitValuation:
                drawToValuationStyle(canvas);
                break;
        }
    }

    private void drawToConfirmStyle(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawBitmap(mBitmapDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                mTextPaint.setColor(getResources().getColor(R.color.color_000000));
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                mLinePaint.setColor(getResources().getColor(R.color.color_F9B5BC));
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
            }
            if (i == 1) {
                //画bitmap
                canvas.drawBitmap(mBitmapToConfirm, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                //画文字
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                //画线
                mLinePaint.setColor(getResources().getColor(R.color.color_4D000000));
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
                //画时间
                float textTimeW = mTextPaint.measureText(mOrderTime);
                int textTimeStart = (int) (mMarginLeft + mBitmapRect.width() / 2 - textTimeW / 2);
                canvas.drawText(mOrderTime, textTimeStart + i * mPointDistance, mViewH / 2 + mBitmapRect.height()+mGapH, mOrderTimePaint);
            }

            if (i == 2) {
                canvas.drawBitmap(mBitmapUnDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                mTextPaint.setColor(getResources().getColor(R.color.color_4D000000));
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);

                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
            }
            if (i == 3) {
                canvas.drawBitmap(mBitmapUnDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
            }

        }
    }

    private void drawToFinishStyle(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawBitmap(mBitmapDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                mTextPaint.setColor(getResources().getColor(R.color.color_000000));
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                //画线
                mLinePaint.setColor(getResources().getColor(R.color.color_F9B5BC));
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
            }
            if (i == 1) {
                canvas.drawBitmap(mBitmapDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);

                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);

            }

            if (i == 2) {
                canvas.drawBitmap(mBitmapToConfirm, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                //画线
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                mLinePaint.setColor(getResources().getColor(R.color.color_4D000000));
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
                //时间
                float textTimeW = mTextPaint.measureText(mOrderTime);
                int textTimeStart = (int) (mMarginLeft + mBitmapRect.width() / 2 - textTimeW / 2);
                canvas.drawText(mOrderTime, textTimeStart + i * mPointDistance, mViewH / 2 + mBitmapRect.height() + mGapH, mOrderTimePaint);
            }
            if (i == 3) {
                canvas.drawBitmap(mBitmapUnDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                mTextPaint.setColor(getResources().getColor(R.color.color_4D000000));
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
            }

        }
    }


    private void drawToValuationStyle(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawBitmap(mBitmapDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                //画线
                mLinePaint.setColor(getResources().getColor(R.color.color_F9B5BC));
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
            }
            if (i == 1) {
                canvas.drawBitmap(mBitmapDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);

            }

            if (i == 2) {
                canvas.drawBitmap(mBitmapDone, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
                canvas.drawLine(mStartX + i * mPointDistance, mLineY, mStopX + i * mPointDistance, mLineY, mLinePaint);
                //时间
                float textTimeW = mTextPaint.measureText(mOrderTime);
                int textTimeStart = (int) (mMarginLeft + mBitmapRect.width() / 2 - textTimeW / 2);
                canvas.drawText(mOrderTime, textTimeStart + i * mPointDistance, mViewH / 2 + mBitmapRect.height() + mGapH, mOrderTimePaint);
            }
            if (i == 3) {
                canvas.drawBitmap(mBitmapToConfirm, mMarginLeft + i * mPointDistance, mBitmapY, mBitmapPaint);
                float textW = mTextPaint.measureText(mStrings[i]);
                int textX = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
                canvas.drawText(mStrings[i], textX + i * mPointDistance, mBitmapY - mGapH, mTextPaint);
            }

        }
    }

    private int measureWidth(int widthMeasureSpec) {
        int resultWidth = 0;
        int widthMeasureMode = getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMeasureMode == MeasureSpec.EXACTLY) {
            resultWidth = widthMeasureSize;

        } else {
            resultWidth = 200;
            if (widthMeasureMode == MeasureSpec.AT_MOST) {
                resultWidth = Math.min(resultWidth, widthMeasureSize);
            }

        }
        return resultWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        int resultHeight = 0;
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMeasureMode == MeasureSpec.EXACTLY) {
            resultHeight = heightMeasureSize;
        } else {
            resultHeight = 100;
            if (heightMeasureMode == MeasureSpec.AT_MOST) {
                resultHeight = Math.min(resultHeight, heightMeasureSize);
            }
        }
        return resultHeight;

    }


    protected int dip2px(float dpValue) {
        return (int) (dpValue * getResources().getDisplayMetrics().density + 0.5f);
    }

    protected int sp2px(int sp) {
        return (int) (sp * getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public enum OrderState {
        waitConfirm,//待确认
        waitFinish,//待完成
        waitValuation//待评价

    }

    private void drawOrder1(Canvas canvas) {

        int viewW = getMeasuredWidth();
        int viewH = getMeasuredHeight();
//        Log.e("bench","-viewW->"+viewW);
//        Log.e("bench","-viewH->"+viewH);
        mLineLength = (viewW - mMarginLeft - mMarginRight - 4 * mBitmapRect.width() - 6 * mGapLength) / 3;
        int bitmapInCenter = viewH / 2 - mBitmapRect.height() / 2;
        int lineInCenter = viewH / 2 + mLineH / 2;
        int itemBitmap = 2 * mGapLength + mLineLength + mBitmapRect.width();

        int startX = mMarginLeft + mGapLength + mBitmapRect.width();
        int stopX = mMarginLeft + mGapLength + mBitmapRect.width() + mLineLength;

//        Log.e("bench", "-mBitmapRect.width()->" + mBitmapRect.width());
        int ItemLine = 2 * mGapLength + mBitmapRect.width() + mLineLength;
        //
        float textW = mTextPaint.measureText(mStrings[0]);
        int textStart = (int) (mMarginLeft + mBitmapRect.width() / 2 - textW / 2);
        int itemText = mBitmapRect.width() + 2 * mGapLength + mLineLength;
        //
        float textTimeW = mTextPaint.measureText(mOrderTime);
        int textTimeStart = (int) (mMarginLeft + mBitmapRect.width() / 2 - textTimeW / 2);

//        for (int i = 0; i < 4; i++) {
//            //画bitmap
//            canvas.drawBitmap(mBitmap, mMarginLeft + i * itemBitmap, bitmapInCenter, mPaint);
//           //字位置
//            canvas.drawText(mStrings[i],textStart+i*itemText,bitmapInCenter-10,mTextPaint);
//            //时间
//            if (i==2)
//             canvas.drawText(mStringTime,textTimeStart+i*itemText,bitmapInCenter+50,mTextPaint);
//            if (i == 3)
//                continue;
//            //画线
//            canvas.drawLine(startX + i * ItemLine, lineInCenter, stopX + i * ItemLine, lineInCenter, mLinePaint);
//        }

        for (int i = 0; i < 4; i++) {
            switch (mOrderType) {
                case waitConfirm:
                    //style 1
                    //画bitmap

                    if (i == 0)
                        canvas.drawBitmap(mBitmapDone, mMarginLeft + i * itemBitmap, bitmapInCenter, mBitmapPaint);
                    if (i == 1)
                        canvas.drawBitmap(mBitmapDone, mMarginLeft + i * itemBitmap, bitmapInCenter, mBitmapPaint);
                    if (i == 2 || i == 3)
                        canvas.drawBitmap(mBitmapDone, mMarginLeft + i * itemBitmap, bitmapInCenter, mBitmapPaint);


                    break;
                case waitFinish:
                    break;
                case waitValuation:
                    break;
            }
            //画bitmap
//            canvas.drawBitmap(mBitmap, mMarginLeft + i * itemBitmap, bitmapInCenter, mPaint);
            //字位置
            canvas.drawText(mStrings[i], textStart + i * itemText, bitmapInCenter - 10, mTextPaint);
            //时间
            if (i == 2)
                canvas.drawText(mOrderTime, textTimeStart + i * itemText, bitmapInCenter + 50, mTextPaint);
            if (i == 3)
                continue;
            //画线
            canvas.drawLine(startX + i * ItemLine, lineInCenter, stopX + i * ItemLine, lineInCenter, mLinePaint);
        }
    }

}

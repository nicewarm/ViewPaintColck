package nicewarm.com.viewpaintclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by sreay on 15/9/20.
 */
public class ViewClock extends View {


    public ViewClock(Context context) {
        super(context);
        init(context);
    }

    public ViewClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        circlePaint = new Paint();
        circlePaint.setColor(color);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(circleStroke);

        paintDegreee = new Paint();
        paintDegreee.setColor(color);
        paintDegreee.setStyle(Paint.Style.FILL);
        paintDegreee.setAntiAlias(true);
        paintDegreee.setStrokeWidth(3);
        updateTime();
        handle = new NiceHandle();

    }

    public void updateTime (){
        timeStr = getDateToStringLong(System.currentTimeMillis());
        String[] timeArray = timeStr.split(":");
        for (int i = 0; i < timeArray.length; i++) {
            time[i] = Integer.parseInt(timeArray[i]);
        }
        /*重绘*/
        invalidate();
    }

    private class NiceHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*更新时间*/
            updateTime();
        }
    }

    /* 时间戳转换成时分秒 */
    public static String getDateToStringLong(long time) {
        try {
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
            return sf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    Paint circlePaint;
    private int mWidth;
    private int mHeight;
    private int circleStroke = 10;
    private int lineStroke = 3;
    private int lineLength ;
    private int strongLineStroke = 5;
    private int strongLineLength;
    private int color = Color.parseColor("#0096db");
    private Paint paintDegreee;
    private int circlePointRadios = 10;
    private String timeStr;
    private int[] time = new int[3];
    private int distanceFromCenter = 20;
    private int distanceSecondFromEdge ;
    private int distanceMinFromEdge ;
    private int distanceHourFromEdge ;
    private NiceHandle handle;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        distanceSecondFromEdge = mWidth/2/10*4;
        distanceMinFromEdge = mWidth/2/10*5;
        distanceHourFromEdge = mWidth/2/10*6;
        strongLineLength = mWidth/2/10*2;
        lineLength = mWidth/2/10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        /*画大圈*/
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - circleStroke, circlePaint);

        /*画刻度线*/
        for (int i = 0; i < 60; i++) {
            /*画有刻度的线*/
            if (i % 5 == 0) {
                /*设置线的宽度*/
                paintDegreee.setStrokeWidth(strongLineStroke);
                /*刻度字体的大小*/
                paintDegreee.setTextSize(30);
                /*画线*/
                canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2 + strongLineStroke, mWidth / 2, mHeight / 2 - mWidth / 2 + strongLineLength, paintDegreee);
                int degreeValue = i / 5 == 0 ? 12 : i / 5;
                String degree = String.valueOf(degreeValue);
                /*画文字*/
                canvas.drawText(degree, mWidth / 2 - paintDegreee.measureText(degree) / 2,
                        mHeight / 2 - mWidth / 2 + strongLineLength + 50, paintDegreee);
            } else {
                /*画无刻度的线*/
                paintDegreee.setStrokeWidth(lineStroke);
                canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2 + lineStroke, mWidth / 2, mHeight / 2 - mWidth / 2 + lineLength, paintDegreee);
            }
            /*旋转6°*/
            canvas.rotate(6, mWidth / 2, mHeight / 2);
        }
        /*恢复画布*/
        canvas.restore();


        /*画圆心点*/
        canvas.drawCircle(mWidth / 2, mHeight / 2, circlePointRadios, paintDegreee);

        /*画指针*/
        /*秒针*/
        canvas.save();
        /*先旋转180°，因为0°时向下，而表的0°是向上*/
        canvas.rotate(180, mWidth / 2, mHeight / 2);
        int degree = time[2] * 360 / 60;
        /*根据时间旋转角度*/
        canvas.rotate(degree, mWidth / 2, mHeight / 2);
        paintDegreee.setStrokeWidth(10);
        canvas.drawLine(mWidth / 2, mHeight / 2 + distanceFromCenter, mWidth / 2, mHeight / 2 + mWidth / 2 - distanceSecondFromEdge, paintDegreee);
        canvas.restore();

        /*画分针*/
        canvas.save();
        canvas.rotate(180,mWidth/2,mHeight/2);
        degree = time[1] * 360 / 60;
        canvas.rotate(degree, mWidth / 2, mHeight / 2);
        paintDegreee.setStrokeWidth(15);
        canvas.drawLine(mWidth / 2, mHeight / 2 + distanceFromCenter, mWidth / 2, mHeight / 2 + mWidth / 2 - distanceMinFromEdge, paintDegreee);
        canvas.restore();

        /*画时针*/
        canvas.save();
        canvas.rotate(180,mWidth/2,mHeight/2);
        degree = time[0] * 360 / 12;
        canvas.rotate(degree, mWidth / 2, mHeight / 2);
        paintDegreee.setStrokeWidth(20);
        canvas.drawLine(mWidth / 2, mHeight / 2 + distanceFromCenter, mWidth / 2, mHeight / 2 + mWidth / 2 - distanceHourFromEdge, paintDegreee);
        canvas.restore();

        handle.sendEmptyMessage(0);
    }
}

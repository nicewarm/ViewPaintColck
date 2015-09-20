package nicewarm.com.viewpaintclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

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


    }

    Paint circlePaint;
    private int mWidth;
    private int mHeight;
    private int circleStroke = 10;
    private int lineStroke = 3;
    private int lineLength = 60;
    private int strongLineStroke = 5;
    private int strongLineLength = 100;
    private int color = Color.parseColor("#0096db");
    private Paint paintDegreee;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画大圈*/
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - circleStroke, circlePaint);

        /*画刻度线*/
        for (int i = 0; i < 60; i++) {
            if (i%5==0) {
                paintDegreee.setStrokeWidth(strongLineStroke);
                paintDegreee.setTextSize(30);
                canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2 + strongLineStroke, mWidth / 2, mHeight / 2 - mWidth / 2 + strongLineLength, paintDegreee);
                int degreeValue = i/5==0?12:i/5;
                String degree = String.valueOf(degreeValue);
                canvas.drawText(degree,mWidth/2-paintDegreee.measureText(degree)/2,
                        mHeight/2-mWidth/2+strongLineLength+50,paintDegreee);
            }else {
                paintDegreee.setStrokeWidth(lineStroke);
                canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2 + lineStroke, mWidth / 2, mHeight / 2 - mWidth / 2 + lineLength, paintDegreee);
            }
            canvas.rotate(6, mWidth / 2, mHeight / 2);
        }

    }
}

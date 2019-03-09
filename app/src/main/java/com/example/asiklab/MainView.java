package com.example.asiklab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView {

    private Paint paint;
    private SurfaceHolder holder;
    private Point point;

    private Canvas canvas;

    private float v0x, v0y, vx, vy, vector_x, vector_y, k;

    public MainView(Context context) {
        super(context);
        init();
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        paint = new Paint();
        holder = getHolder();
    }

    void init(int x, int y, float v0x, float voy) {
        vx = this.v0x = v0x / 100;
        vy = this.v0y = voy / 100;
        k = 1;
        init();
        vector_x = x / 100f;
        vector_y = y / 100f;
        point = new Point(50, 50);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);

        Log.e("mylog", "eee");
        Log.d("mylog", "init() called with: x = [" + x + "], y = [" + y + "], v0x = [" + v0x + "], voy = [" + voy + "]");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        canvas = holder.lockCanvas();
                        if(canvas != null) {
                            canvas.drawColor(Color.WHITE);
                            canvas.drawPoint(point.x, point.y, paint);
                            holder.unlockCanvasAndPost(canvas);
                            break;
                        }
                        holder.unlockCanvasAndPost(null);
                        Thread.sleep(10);
                    } catch (Exception e) {
                        Log.e("mylog", "thread: " + e.toString());
                    }
                }
            }
        }.start();
    }

    void changeProgress(int progress) {
        point.x = (int) ((v0x*progress + vector_x*progress*progress)*k + 50);
        point.y = (int) ((v0y*progress + vector_y*progress*progress)*k + 50);
        vx = (v0x + vector_x*progress)*k;
        vy = (v0y + vector_y*progress)*k;
        canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            canvas.drawPoint(point.x, point.y, paint);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLUE);
            canvas.drawLine(point.x, point.y, point.x + vx*2, point.y + vy*2, paint);
            paint.setStrokeWidth(100);
            paint.setColor(Color.BLACK);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    void plus() {
        if(k >= 0.1)
            k+=0.1;
        else
            k+=0.05;
        Log.i("mylog", "k: " + k);
    }

    void minus() {
        if(k > 0.1)
            k-=0.1;
        else
            k-=0.05;
        Log.i("mylog", "k: " + k);
    }
}

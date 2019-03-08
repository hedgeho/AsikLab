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
    private Point m_vector, point;

    private Canvas canvas;

    private float time, vx, vy;

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
        m_vector = null;
    }

    void init(int x, int y, float v0x, float voy) {
        vx = v0x / 100;
        vy = voy / 100;
        time = 0;
        init();
        m_vector = new Point(x / 100, y  / 100);
        point = new Point(getWidth()/2, 50);

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
                        if(getWidth() > 0 && point.x <= 0) {
                            point.x = getWidth()/2;
                        } else {
                            canvas = holder.lockCanvas();
                            if (canvas != null) {
                                canvas.drawColor(Color.WHITE);
                                canvas.drawPoint(point.x, point.y, paint);
                                holder.unlockCanvasAndPost(canvas);
                            }
                            vx += m_vector.x;
                            vy += m_vector.y;
                            point.x += vx;
                            point.y += vy;
                            Log.e("mylog", "vx: " + vx + ", vy: " + vy + ", x: " + point.x + ", y: " + point.y);
                        }
                        Thread.sleep(10);
                    } catch (Exception e) {
                        Log.e("mylog", e.toString());
                    }
                }
            }
        }.start();
    }

}

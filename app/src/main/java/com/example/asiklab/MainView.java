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
import android.webkit.WebHistoryItem;
import android.widget.EditText;

import java.util.Locale;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint;
    private SurfaceHolder holder;

    boolean running = false;

    EditText et_v, et_t, et_x, et_y;

    private Canvas canvas;

    private double t, v0x, v0y, vx, vy, vector_x, vector_y, point_x, point_y, line_y, k;

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

    void init1() {
        k = 1;
        init();
        line_y = -1;
        point_x = 0;
        point_y = 0;

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);

        Log.e("mylog", "eee");
        //Log.d("mylog", "init() called with: x = [" + x + "], y = [" + y + "], v0x = [" + v0x + "], v0y = [" + v0y + "]");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        canvas = holder.lockCanvas();
                        if(canvas != null) {
                            canvas.drawColor(Color.WHITE);
                            canvas.drawPoint((float) point_x + 50, (float) point_y + 50, paint);
                            paint.setStrokeWidth(5);
                            t = (-v0x/10d + Math.sqrt(v0x*v0x/100d + 2*vector_x*getWidth()))/vector_x;
                            line_y = v0y*t/10 + vector_y*t*t/2;
                            canvas.drawLine(50, 50, getWidth(), (float) (v0y*t/10 + vector_y*t*t/2), paint);
                            holder.unlockCanvasAndPost(canvas);
                        }
                        break;
                    } catch (Exception e) {
                        Log.e("mylog", "thread: " + e.toString());
                    }
                }
            }
        }.start();
    }

    void init(int x, int y, final int v0x, final int v0y) {
        vx = this.v0x = v0x / 10d;
        vy = this.v0y = v0y / 10d;
        vector_x = x / 100d;
        vector_y = y / 100d;
    }

    void play() {
        new Thread() {
            @Override
            public void run() {
                t = 0;
                vx = v0x;
                vy = v0y;
                point_x = 0;
                point_y = 0;
                running = true;
                while(true) {
                    if(running) {
                        try {

                        /*point_x = v0x*t + vector_x*t*t/2;
                        point_y = v0y*t + vector_y*t*t/2;*/
                            point_x += vx;
                            point_y += vy;
                            vx += vector_x;
                            vy += vector_y;
                            //t = (-v0x + Math.sqrt(v0x * v0x + 2 * vector_x * point_x)) / vector_x;
                            //Log.i("mylog", t + "");
                            t+=1;

                            if (point_x > getWidth() || point_y > getHeight())
                                break;

                            canvas = holder.lockCanvas();
                            if (canvas != null) {
                                canvas.drawColor(Color.WHITE);
                                paint.setColor(Color.BLACK);
                                paint.setStrokeWidth(100);
                                canvas.drawPoint((float) point_x + 50, (float) point_y + 50, paint);
                                paint.setStrokeWidth(5);
                                if (line_y != -1)
                                    canvas.drawLine(50, 50, getWidth(), (float) line_y, paint);
                                else {
                                    t = (-v0x + Math.sqrt(v0x * v0x + 2 * vector_x * getWidth())) / vector_x;
                                    line_y = v0y * t + vector_y * t * t / 2;
                                    canvas.drawLine(50, 50, getWidth(), (float) (v0y * t + vector_y * t * t / 2), paint);
                                }
                                holder.unlockCanvasAndPost(canvas);
                            }
                            if (et_t != null) {
                                MainActivity.costyl = 4;
                                et_t.setText(t + "");
                                et_v.setText(String.format(Locale.ENGLISH, "%f", Math.sqrt(vx * vx + vy * vy)));
                                et_x.setText(point_x + "");
                                et_y.setText(point_y + "");
                            }
                            Thread.sleep(10);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }.start();
    }

    void pause() { running = !running; }

    void setViews(EditText a, EditText b, EditText c, EditText d) {
        et_v = a;
        et_t = b;
        et_x = c;
        et_y = d;
    }

    void setT(double t) {
        //t = t/10;
        point_x = v0x*t + vector_x*t*t/2;
        point_y = v0y*t + vector_y*t*t/2;
        vx = v0x + vector_x*t;
        vy = v0y + vector_y*t;
        Log.i("mylog", "vx: " + vx + ", vy: " + vy + ", vector_x: " + vector_x + ", t: " + t);
        if(et_v != null) {
            MainActivity.costyl = 3;
            et_v.setText(String.format(Locale.ENGLISH, "%f", Math.sqrt(vx*vx + vy*vy)));
            et_x.setText(point_x + "");
            et_y.setText(point_y + "");
        }
        canvas = holder.lockCanvas();
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(100);
            canvas.drawPoint((float) point_x + 50, (float) point_y + 50, paint);
            paint.setStrokeWidth(5);
            if(line_y != -1)
                canvas.drawLine(50, 50 , getWidth(), (float) line_y, paint);
            else {
                t = (-v0x + Math.sqrt(v0x*v0x + 2*vector_x*getWidth()))/vector_x;
                line_y = v0y*t + vector_y*t*t/2;
                canvas.drawLine(50, 50, getWidth(), (float) (v0y*t + vector_y*t*t/2), paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    void setX(double x) {
        point_x = x;
        t = (-v0x + Math.sqrt(v0x*v0x + 2*vector_x*x))/vector_x;
        point_y = v0y*t + vector_y*t*t/2;
        vx = v0x + vector_x*t;
        vy = v0y + vector_y*t;
        Log.e("mylog", "1");
        if (et_v != null) {
            MainActivity.costyl = 3;
            et_v.setText(String.format(Locale.ENGLISH, "%f", Math.sqrt(vx*vx + vy*vy)));
            et_t.setText(t + "");
            et_y.setText(point_y + "");
        }
        canvas = holder.lockCanvas();
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(100);
            canvas.drawPoint((float) point_x + 50, (float) point_y + 50, paint);
            paint.setStrokeWidth(5);
            if(line_y != -1)
                canvas.drawLine(50, 50 , getWidth(), (float) (line_y), paint);
            else {
                t = (-v0x + Math.sqrt(v0x*v0x + 2*vector_x*getWidth()))/vector_x;
                line_y = v0y*t + vector_y*t*t/2;
                canvas.drawLine(50, 50, getWidth(), (float) (v0y*t + vector_y*t*t/2), paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    void setY(double y) {
        point_y = y;
        t = (-v0y + Math.sqrt(v0y*v0y + 2*vector_y*y))/vector_y;
        point_x = v0x*t + vector_x*t*t/2;
        vx = v0x + vector_x*t;
        vy = v0y + vector_y*t;
        Log.e("mylog", "1");
        if (et_v != null) {
            MainActivity.costyl = 3;
            et_v.setText(String.format(Locale.ENGLISH, "%f", Math.sqrt(vx*vx + vy*vy)));
            et_t.setText(t + "");
            et_x.setText(point_x + "");
        }
        canvas = holder.lockCanvas();
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(100);
            canvas.drawPoint((float) point_x + 50, (float) point_y + 50, paint);
            paint.setStrokeWidth(5);
            if(line_y != -1)
                canvas.drawLine(50, 50 , getWidth(), (float) line_y, paint);
            else {
                t = (-v0x + Math.sqrt(v0x*v0x + 2*vector_x*getWidth()))/vector_x;
                line_y = v0y*t + vector_y*t*t/2;
                canvas.drawLine(50, 50, getWidth(), (float) (v0y*t + vector_y*t*t/2), paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    void setV(double v) {
        t = (v - Math.sqrt(v0x*v0x + v0y*v0y)) / Math.sqrt(vector_x*vector_x + vector_y*vector_y);
        point_x = v0x*t + vector_x*t*t/2;
        point_y = v0y*t + vector_y*t*t/2;
        Log.e("mylog", "1");
        if (et_v != null) {
            MainActivity.costyl = 3;
            et_t.setText(t + "");
            et_x.setText(point_x + "");
            et_y.setText(point_y + "");
        }
        canvas = holder.lockCanvas();
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(100);
            canvas.drawPoint((float) point_x + 50, (float) point_y + 50, paint);
            paint.setStrokeWidth(5);
            if(line_y != -1)
                canvas.drawLine(50, 50 , getWidth(), (float) line_y, paint);
            else {
                t = (-v0x/100 + Math.sqrt(v0x*v0x/10000 + 2*vector_x*getWidth()))/vector_x;
                line_y = v0y*t + vector_y*t*t/2;
                canvas.drawLine(50, 50, getWidth(), (float) (v0y*t + vector_y*t*t/2), paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    void changeProgress(int progress) {
        //point.x = (int) ((v0x*progress + vector_x*progress*progress)*k + 50);
        //point.y = (int) ((v0y*progress + vector_y*progress*progress)*k + 50);
        vx = (v0x + vector_x*progress)*k;
        vy = (v0y + vector_y*progress)*k;
        canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            //canvas.drawPoint(point.x, point.y, paint);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLUE);
            //canvas.drawLine(point.x, point.y, point.x + vx*2, point.y + vy*2, paint);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        init();
        init1();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

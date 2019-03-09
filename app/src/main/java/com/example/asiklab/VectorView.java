package com.example.asiklab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.asiklab.VectorActivity.log;

public class VectorView extends SurfaceView {

    private Point point;
    private Paint paint;
    private SurfaceHolder holder;
    private ArrayList<Point> vectors;
    private Point m_vector;
    private int current_vector;

    private TextView tv_length, tv_angle, tv_length_b, tv_angle_b;

    private Canvas canvas;

    public VectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public VectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public VectorView(Context context) {
        super(context);
        init();
    }

    void init() {
        point = new Point();
        vectors = new ArrayList<>();
        m_vector = null;
        current_vector = -1;
        paint = new Paint();
        holder = getHolder();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);
        tv_length = null;
        tv_angle = null;

        new Thread() {
            @Override
            public void run() {
                boolean boo = false;
                while(true) {
                    try {
                        if(getWidth() > 0 && point.x <= 0) {
                            point.x = getWidth() / 2;
                            point.y = getHeight() / 2;
                            boo = true;
                        }
                        canvas = holder.lockCanvas();
                        if(canvas!=null && boo) {
                            canvas.drawColor(Color.WHITE);
                            paint.setStrokeWidth(100);
                            paint.setColor(Color.BLACK);
                            canvas.drawPoint(point.x, point.y, paint);
                            holder.unlockCanvasAndPost(canvas);
                            break;
                        }

                        Thread.sleep(50);
                    } catch (Exception e) {
                        Log.e("mylog", e.toString());
                    }
                }
            }
        }.start();
    }

    void setViews(TextView length, TextView angle, TextView b1, TextView b2) {
        tv_length = length;
        tv_angle = angle;
        tv_length_b = b1;
        tv_angle_b = b2;
    }

    void delete() {
        if(current_vector != -1) {
            vectors.remove(current_vector);
            current_vector = -1;
        }
        canvas = holder.lockCanvas();
        float sumx = 0, sumy = 0;
        for (Point vector: vectors) {
            sumx += vector.x - point.x;
            sumy += vector.y - point.y;
        }
        if(m_vector == null) {
            m_vector = new Point();
        }
        m_vector.x = (int) sumx + point.x;
        m_vector.y = (int) sumy + point.y;

        if(canvas!=null) {
            canvas.drawColor(Color.WHITE);
            paint.setStrokeWidth(100);
            paint.setColor(Color.BLACK);
            canvas.drawPoint(point.x, point.y, paint);
            paint.setStrokeWidth(5);
            for (Point vector : vectors) {
                if (vectors.indexOf(vector) == current_vector)
                    paint.setColor(Color.RED);
                else
                    paint.setColor(Color.BLACK);
                canvas.drawLine(point.x, point.y, vector.x, vector.y, paint);
            }
            if(m_vector != null) {
                paint.setColor(Color.BLUE);
                canvas.drawLine(point.x, point.y, m_vector.x, m_vector.y, paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
        if(tv_angle != null) {
            tv_angle.setText("angle: 0");
        }
        if(tv_length != null)
            tv_length.setText("length: 0");
        if (tv_angle_b != null)
            tv_angle_b.setText("angle: 0");
        if (tv_length_b != null)
            tv_length_b.setText("length: 0");
    }

    void deleteAll() {
        vectors = new ArrayList<>();
        m_vector = null;
        current_vector = -1;
        if (tv_length != null) {
            tv_length.setText("length: 0");
        }
        if (tv_angle != null) {
            tv_angle.setText("angle: 0");
        }
        if (tv_angle_b != null)
            tv_angle_b.setText("angle: 0");
        if (tv_length_b != null)
            tv_length_b.setText("length: 0");
        canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setStrokeWidth(100);
            paint.setColor(Color.BLACK);
            canvas.drawPoint(point.x, point.y, paint);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX(), y = (int) event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            log("DOWN");
            Point vector;
            boolean neww = true;
            for (int i = 0;  i < vectors.size(); i++) {
                vector = vectors.get(i);
                if (vector.x <= x+50 && vector.x >= x-50 && vector.y <= y+50 && vector.y >= y-50) {
                    current_vector = i;
                    neww = false;
                }
            }
            if(neww) {
                vectors.add(new Point(x, y));
                current_vector = vectors.size()-1;
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP){
            if(current_vector != -1 && x > 0 && x <= getWidth() && y > 0 && y <= getHeight()) {
                vectors.get(current_vector).x = x;
                vectors.get(current_vector).y = y;
            }
        }
        if(current_vector != -1) {
            float sumx = 0, sumy = 0;
            for (Point vector: vectors) {
                sumx += vector.x - point.x;
                sumy += vector.y - point.y;
            }
            if(m_vector == null) {
                m_vector = new Point();
            }
            m_vector.x = (int) sumx + point.x;
            m_vector.y = (int) sumy + point.y;
        }
        if(point.x > 0) {
            draw();
        }
        return true;
    }

    int getx() {
        return m_vector.x - point.x;
    }
    int gety() {
        return m_vector.y-point.y;
    }

    void draw() {
        new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(10);
                        canvas = holder.lockCanvas();
                        if (canvas != null) {
                            canvas.drawColor(Color.WHITE);
                            paint.setStrokeWidth(100);
                            paint.setColor(Color.BLACK);
                            canvas.drawPoint(point.x, point.y, paint);
                            paint.setStrokeWidth(5);
                            for (Point vector : vectors) {
                                if (vectors.indexOf(vector) == current_vector)
                                    paint.setColor(Color.RED);
                                else
                                    paint.setColor(Color.BLACK);
                                canvas.drawLine(point.x, point.y, vector.x, vector.y, paint);
                            }
                            if (m_vector != null) {
                                paint.setColor(Color.BLUE);
                                canvas.drawLine(point.x, point.y, m_vector.x, m_vector.y, paint);
                            }
                            holder.unlockCanvasAndPost(canvas);
                        } else {
                            holder.unlockCanvasAndPost(null);
                            continue;
                        }
                        if (current_vector == -1)
                            break;
                        double length = Math.sqrt((vectors.get(current_vector).x - point.x) * (vectors.get(current_vector).x - point.x) +
                                (vectors.get(current_vector).y - point.y) * (vectors.get(current_vector).y - point.y));
                        if (tv_length != null) {
                            tv_length.setText(String.format("length: %.4f", length));
                        }
                        if (tv_angle != null) {
                            double ang = Math.asin((vectors.get(current_vector).x - point.x) / length) * 180 / Math.PI;
                            if (vectors.get(current_vector).y - point.y > 0)
                                ang = 180 - ang;
                            else if (ang < 0)
                                ang = 360 + ang;
                            tv_angle.setText(String.format("angle: %.4f", ang));
                        }
                        double length1 = Math.sqrt((m_vector.x - point.x) * (m_vector.x - point.x) +
                                (m_vector.y - point.y) * (m_vector.y - point.y));
                        if (tv_angle_b != null) {
                            double ang = Math.asin((m_vector.x - point.x) / length1) * 180 / Math.PI;
                            if (m_vector.y - point.y > 0)
                                ang = 180 - ang;
                            else if (ang < 0)
                                ang = 360 + ang;
                            tv_angle_b.setText(String.format("angle: %.4f", ang));
                        }
                        if (tv_length_b != null) {
                            tv_length_b.setText(String.format("length: %.4f", length));
                        }
                        break;
                    } catch (Exception e) {
                        Log.e("mylog", e.toString());
                    }
                }
            }
        }.start();
    }
}

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.asiklab.VectorActivity.log;

public class VectorView extends SurfaceView implements SurfaceHolder.Callback {

    private boolean speed;

    private Point point;
    private Paint paint;
    private SurfaceHolder holder;
    private ArrayList<Vector> vectors;
    private Point m_vector;
    private int current_vector, count;

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
        speed = false;
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
        count = 0;

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
                        if(canvas!=null) {
                            canvas.drawColor(Color.WHITE);
                            paint.setStrokeWidth(100);
                            paint.setColor(Color.BLACK);
                            canvas.drawPoint(point.x, point.y, paint);
                            holder.unlockCanvasAndPost(canvas);
                            break;
                        }
                        if(boo) break;

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
        for (Vector vector: vectors) {
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
            for (Vector vector : vectors) {
                if (vectors.indexOf(vector) == current_vector)
                    paint.setColor(Color.RED);
                else
                    paint.setColor(Color.BLACK);
                canvas.drawLine(point.x, point.y, vector.x, vector.y, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(100);
                canvas.drawText(vector.name, vector.x, vector.y, paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
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
        count = 0;
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
            Vector vector;
            boolean neww = true;
            for (int i = 0;  i < vectors.size(); i++) {
                vector = vectors.get(i);
                if (vector.x <= x+50 && vector.x >= x-50 && vector.y <= y+50 && vector.y >= y-50) {
                    current_vector = i;
                    neww = false;
                }
            }
            if(neww && (vectors.size() < 1 || !speed)) {
                if(speed)
                    vectors.add(new Vector("v", x, y));
                else
                    vectors.add(new Vector("F" + ++count, x, y));
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
            for (Vector vector: vectors) {
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
            draw(false);
        }
        return true;
    }

    int getx() {
        return m_vector.x - point.x;
    }
    int gety() {
        int var = m_vector.y-point.y;
        if(!speed) {
            speed = true;
            deleteAll();
        }
        return var;
    }

    void draw(boolean sleep) {
        try {
            if(sleep)
                Thread.sleep(100);
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                paint.setStrokeWidth(100);
                paint.setColor(Color.BLACK);
                canvas.drawPoint(point.x, point.y, paint);
                paint.setStrokeWidth(5);
                for (Vector vector : vectors) {
                    if (vectors.indexOf(vector) == current_vector)
                        paint.setColor(Color.RED);
                    else
                        paint.setColor(Color.BLACK);
                    canvas.drawLine(point.x, point.y, vector.x, vector.y, paint);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(100);
                    canvas.drawText(vector.name, vector.x, vector.y, paint);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5);
                }
                if (m_vector != null && !speed) {
                    paint.setColor(Color.BLUE);
                    canvas.drawLine(point.x, point.y, m_vector.x, m_vector.y, paint);
                }
                holder.unlockCanvasAndPost(canvas);
            }
            if (current_vector == -1)
                return;

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
                /*if (m_vector.y - point.y > 0)
                    ang = 180 - ang;
                else if (ang < 0)
                    ang = 360 + ang;*/
                tv_angle_b.setText(String.format("angle: %.4f", ang));
            }
            if (tv_length_b != null) {
                tv_length_b.setText(String.format("length: %.4f", length1));
            }

            if(speed && tv_length_b != null) {
                tv_length_b.setText("");
                tv_angle_b.setText("");
            }
        } catch (Exception e) {
            Log.e("mylog", e.toString());
        }
    }

    String getData() {
        if(current_vector != -1) {
            try {
                Vector v = vectors.get(current_vector);
                JSONObject obj = new JSONObject()
                        .put("name", v.name);
                double length = Math.sqrt((v.x - point.x) * (v.x - point.x) +
                        (v.y - point.y) * (v.y - point.y));
                double ang = Math.asin((v.x - point.x) / length) * 180 / Math.PI;
                if (v.y - point.y > 0)
                    ang = 180 - ang;
                else if (ang < 0)
                    ang = 360 + ang;
                obj.put("length", length)
                        .put("angle", ang);
                return obj.toString();
            } catch (JSONException e) {}
        }
        return "";
    }

    void setData(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            Vector v = vectors.get(current_vector);
            v.name = obj.getString("name");
            double l = obj.getDouble("length"),
                    a = obj.getDouble("angle");
            if(a > 270)
                a = a - 360;
            else if(a > 90)
                a = 180 - a;
            log("a: " + a);
            a = a*Math.PI / 180;
            double x = l*Math.sin(a), y = l*Math.cos(a);
            log(x + "; " + y);
            v.x = (int) x + point.x;
            v.y = (int) y + point.y;
            draw(false);
        } catch (JSONException e) {
            log(e.toString());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        init();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class Vector {
        int x, y;
        String name;

        Vector(String n, int x, int y) {
            name = n;
            this.x = x;
            this.y = y;
        }
    }
}

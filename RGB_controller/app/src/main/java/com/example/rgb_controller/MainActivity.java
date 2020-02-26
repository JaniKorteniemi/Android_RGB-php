package com.example.rgb_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

public class MainActivity extends AppCompatActivity {


    ImageView colorWheel;
    View colorGradient;
    View colorResult;
    TextView textResult;
    TextView connectText;

    Bitmap colorBitmap;

    int pixel = 0;
    String Hex;
    int r, g, b;
    int gratedR, gratedG, gratedB;
    double newr = 0, newg = 0, newb = 0;
    int tempGradientValue = 127;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorWheel = findViewById(R.id.colorWheel);
        colorGradient = findViewById(R.id.gradientColor);
        colorResult = findViewById(R.id.outputColor);
        textResult = findViewById(R.id.outputTextView);
        connectText = findViewById(R.id.mySQLConnection);

        colorWheel.setDrawingCacheEnabled(true);
        colorWheel.buildDrawingCache(true);

        //(ImageView) colorWheel on thouch listener
        colorWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    colorBitmap = colorWheel.getDrawingCache();


                    try{
                        pixel = colorBitmap.getPixel((int)event.getX(), (int)event.getY());
                    } catch (Exception e) {
                        e.printStackTrace();
                        pixel = 0;
                    }
                    Log.v("HEX value", Integer.toHexString(pixel) + ", " + Integer.toHexString(0) + ", " + pixel);

                    if (pixel != 0){
                        //Get Hex value
                        Hex = "#"+ Integer.toHexString(pixel);

                        //Get R G B
                        r = Color.red(pixel);
                        g = Color.green(pixel);
                        b = Color.blue(pixel);

                        gratedR = r;
                        gratedG = g;
                        gratedB = b;

                        textResult.setText("HEX: " + Hex + "\nRGB: " +r + ", " + g + ", " + b);


                        int h = colorGradient.getHeight();
                        ShapeDrawable sGradient = new ShapeDrawable(new RectShape());
                        sGradient.getPaint().setShader(new LinearGradient(0,0,0,h, Color.rgb(gratedR,gratedG,gratedB), Color.BLACK, Shader.TileMode.REPEAT));
                        colorGradient.setBackgroundDrawable(sGradient);
                        colorResult.setBackgroundColor(Color.rgb(r,g,b));

                    }
                }
                return true;
            }
        });

        colorGradient.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    colorBitmap = colorGradient.getDrawingCache();

                   // double Gradient =

                    int y = (int)event.getY();

                    if (y <= colorGradient.getHeight() && y >= 0) {

                        double gradientPercentage =  1 - ((double) y / (double) colorGradient.getHeight());
                        double gradientdouble= (gradientPercentage * 255);
                        int gradientValue = (int) gradientdouble;

                        double vector = Math.sqrt(Math.pow((double)r, 2.0) + Math.pow((double)g, 2) + Math.pow((double)b, 2));

                        double sinR = Math.cos(r/vector);
                        double sinG = Math.cos(g/vector);
                        double sinB = Math.cos(b/vector);

                        float rPrecent = (float)r / 255.0f ;
                        float gPrecent = (float)g / 255.0f ;
                        float bPrecent = (float)b / 255.0f ;

                        //if (tempGradientValue > gradientValue){
                        newr = r * gradientPercentage;
                        newg = g * gradientPercentage;
                        newb = b * gradientPercentage;

                       /* gratedR = newr;
                        gratedG = newg;
                        gratedB = newb;*/

                        /*
                        newr = (double)r + (255 - (double)r) * gradientPercentage;
                        newg = (double)g + (255 - (double)g) * gradientPercentage;
                        newb = (double)b + (255 - (double)b) * gradientPercentage;

                        r = r - (int)newr;
                        g = g - (int)newg;
                        b = b - (int)newb;*/
                        /*}
                        else if(tempGradientValue < gradientValue){
                            newr = r * (1 - gradientPercentage);
                            newg = g * (1 - gradientPercentage);
                            newb = b * (1 - gradientPercentage);

                            r = (int)newr;
                            g = (int)newg;
                            b = (int)newb;
                        }*/

                        tempGradientValue = gradientValue;

                        gratedR = (int)newr;
                        gratedG = (int)newg;
                        gratedB = (int)newb;


                        textResult.setText("HEX: " + Hex + "\nRGB: " +(int)newr + ", " + (int)newg + ", " + (int)newb);
                        colorResult.setBackgroundColor(Color.rgb(gratedR,gratedG,gratedB));

                        Log.v("newrgb", "RGB: " + newr + ", " + newg + ", " + newb);
                        Log.v("gratedRGB", "RGB: " + gratedR + ", " + gratedG + ", " + gratedB);
                        Log.v("Percents1", "Percent%:" + gradientPercentage + ", vector:" + vector);
                        Log.v("Percents2", "RGB%:" + rPrecent + ", " + gPrecent + ", " + bPrecent);
                    }
                }
                return true;
            }
        });
        createConnection();
    }
    public void testButton(View v){
        createConnection();
    }

    public void createConnection(){
        //MySQLConnection mySQLConnection = new MySQLConnection(this);
        //mySQLConnection.execute(gratedR, gratedG, gratedB);


    }

    private void sdjustScreen(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
    }
}

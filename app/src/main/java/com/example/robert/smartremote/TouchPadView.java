package com.example.robert.smartremote;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.view.MotionEvent.AXIS_X;

/**
 * Created by yunl on 10/24/2017.
 */

public class TouchPadView extends View {

    GestureDetector gestureDetector;
    Context context;

    float preX;
    float preY;

    CommandManager commandManager;
    String ip;

    public TouchPadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //this.gestureDetector =
        commandManager = new CommandManager();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                preX = eventX;
                preY = eventY;
                return true;
            case MotionEvent.ACTION_MOVE:
                //TODO let the ip check stay in mainActivity
                String ip = ((EditText)getRootView().findViewById(R.id.txt_ip_addr)).getText().toString();
                if(!MainActivity.validate(ip)){
                    return false;
                }
                commandManager.setDestIpAddr(ip);
                if(eventY > preY){
                    //up
                    commandManager.sendScrollUp();
                } else {
                    //down
                    commandManager.sendScrollDown();
                }

            default:
                break;
        }
        return true;
    }

    public void setIpAddr(String ip){
        this.ip = ip;
    }



}

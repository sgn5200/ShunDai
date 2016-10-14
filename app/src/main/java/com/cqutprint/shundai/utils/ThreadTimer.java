package com.cqutprint.shundai.utils;

import android.os.Handler;
import android.os.Message;


public class ThreadTimer {
    private Handler handler;
    private boolean control=true;

    private long time;
    int flag=0;
    public ThreadTimer(Handler handler,long time){
        this.handler=handler;
        this.time=time;
    }

    public void stop( ) {
        this.control = false;
    }

    public void start(){
        control=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (control){
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message=Message.obtain();
                    message.what= ++flag;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}

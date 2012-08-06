package com.supremefist.scrumgm;

import java.util.Date;

public class Timer extends Thread {

    private long currentMs = 0;
    private long lastCheck = 0;
    
    private boolean running = true;
    private boolean timerRunning = false;
    
    public Timer() {
        start();
    }
    
    public void reset() {
        currentMs = 0;
    }
    
    public void startTimer() {
        lastCheck = new Date().getTime();
        timerRunning = true;
    }
    
    public void stopTimer() {
        timerRunning = false;
    }
    
    public void run() {
        while (running) {
            try {
                if (timerRunning) {
                    long newCheck = new Date().getTime(); 
                    currentMs += newCheck - lastCheck;
                    lastCheck = newCheck;
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    
    public long getMs() {
        return currentMs;
    }

    public void terminate() {
        running = false;
    }

}

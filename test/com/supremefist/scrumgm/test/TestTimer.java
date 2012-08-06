package com.supremefist.scrumgm.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.supremefist.scrumgm.Timer;

public class TestTimer {

    
    Timer testTimer = null;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        if (testTimer != null) {
            testTimer.terminate();
        }
        testTimer = null;
    }

    @Test
    public void testInit() {
        Timer t = new Timer();
        
        assertEquals(0, t.getMs());
    }

    private void testWait(long durationMs) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testShortTime() {
        testTimer = new Timer();
        
        testTimer.startTimer();
        
        testWait(500);
        assertTrue(Math.abs(testTimer.getMs() - 500) < 50);
        
        testWait(500);
        testTimer.stopTimer();
        assertTrue(Math.abs(testTimer.getMs() - 1000) < 50);

        testWait(500);
        assertTrue(Math.abs(testTimer.getMs() - 1000) < 50);
        
    }
    
    @Test
    public void testReset() {
        testTimer = new Timer();
        
        testTimer.startTimer();
        
        testWait(500);
        assertTrue(Math.abs(testTimer.getMs() - 500) < 50);
        
        testTimer.reset();
        
        assertEquals(0, testTimer.getMs());
        
    }

    
}

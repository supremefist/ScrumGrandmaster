package com.supremefist.scrumgm;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ScrumGMView extends JFrame implements KeyListener {

    JTextArea displayArea;
    ScrumGM control = null;
    public boolean ready = false;

    public ScrumGMView(ScrumGM newControl) {
        super("ScrumGM");
        control = newControl;

    }
    
    public void createAndShowGUI() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        
        addComponents();

        setVisible(true);
        
        ready = true;
    }



    
    public void showText(String text) {
        displayArea.setText(text);
    }

    private void addComponents() {
        displayArea = new JTextArea();
        displayArea.setText("Welcome to ScrumGM!  Press space to start!");
        displayArea.setEditable(false);
        displayArea.addKeyListener(this);
        
        getContentPane().add(displayArea, BorderLayout.CENTER);
    }
    

    @Override
    public void keyPressed(KeyEvent arg0) {
     
        if (arg0.getKeyCode() == 32) {
            if (control.scrumming) { 
                control.nextTriggered();
            }
            else {
                control.startTriggered();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

}

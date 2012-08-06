package com.supremefist.scrumgm;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ScrumGM {

    static Random random = new Random();
    private final static long scrumDurationMs = 1 * 60 * 1000;
    ScrumGMView view = null;
    private boolean nextTriggered = false;
    public boolean scrummingTriggered = false;
    public boolean scrumming = false;
    private boolean scrumTriggered = false;
    
    /**
     * @param args
     */

    public ScrumGM() {
        view = new ScrumGMView(this);
        view.createAndShowGUI();
        
    }

    public void start() {
        while (true) {
            
            if (scrumTriggered) {
                scrumTriggered = false;
                scrum();
            }
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
    
    public void scrum() {
        scrumming = true;
        List<String> participants = new Vector<String>();
        participants.add("Riaan");
        participants.add("Matthew");
        participants.add("Leendert");
        participants.add("Alex");
        participants.add("John");

        long remainingDurationMs = scrumDurationMs;
        Timer timer = new Timer();

        while (participants.size() > 0) {
            int selectedParticipant = random.nextInt(participants.size());
            boolean warned = false;
            nextTriggered = false;

            long maxDuration = remainingDurationMs / participants.size();
            String participantName = participants.get(selectedParticipant);
            view.showText("Go " + participantName + " for " + maxDuration
                    / 1000 + " seconds...");
            timer.startTimer();
            long personDuration = timer.getMs();

            while ((personDuration < maxDuration) && (!nextTriggered)) {
                personDuration = timer.getMs();

                long remainingForPerson = maxDuration - timer.getMs();

                if ((remainingForPerson < 30 * 1000) && (!warned)) {
                    view.showText("Finish up " + participantName + "!  Less than 30 seconds left!");
                    warned = true;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            view.showText(participantName + " done!");
            timer.stopTimer();
            timer.reset();

            participants.remove(selectedParticipant);
            remainingDurationMs -= personDuration;

        }

        view.showText("Scrum complete!");
        timer.terminate();
        
        scrumming = false;
    }
    
    public static void main(String[] args) {

        // Schedule a job for event dispatch thread:
        // creating and showing this application's GUI.
        final ScrumGM scrummer = new ScrumGM();
        scrummer.start();
    }

    public void startTriggered() {
        if (!scrumming) {
            scrumTriggered  = true;
        }
    }
    
    public void nextTriggered() {
        nextTriggered = true;
   }
}

package com.supremefist.scrumgm;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class ScrumGM {

    VoiceManager voiceManager = VoiceManager.getInstance();
    Voice voice = null;
    
    static Random random = new Random();
    private final static long scrumDurationMs = 5 * 60 * 1000;
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
        
        initializeVoice();

    }
    
    public void initializeVoice() {
        voice = voiceManager.getVoice("kevin16");
        voice.allocate();

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

    public void say(String text) {
        System.out.println("Saying " + text);
        voice.speak(text);

    }
    
    public void destroy() {
        voice.deallocate();
    }
    
    public void scrum() {
        scrumming = true;


        List<String> participants = new Vector<String>();
        participants.add("Riaan");
        participants.add("Matthew");
        participants.add("Leendert");
        participants.add("Alex");
        participants.add("John");
        
        List<String> pronounceNames = new Vector<String>();
        pronounceNames.add("Riaan");
        pronounceNames.add("Matthew");
        pronounceNames.add("Leeuhndirt");
        pronounceNames.add("Alex");
        pronounceNames.add("John");
        
        List<String> quips = new Vector<String>();
        quips.add("thee hilarious and incredibly handsome man");
        quips.add("no surf talk");
        quips.add("our venerable master");
        quips.add("lucky maintainer of the beautiful are ess vee");
        quips.add("the professor in training");
        
        
        long remainingDurationMs = scrumDurationMs;
        Timer timer = new Timer();

        while (participants.size() > 0) {
            int selectedParticipant = random.nextInt(participants.size());
            String participantName = participants.get(selectedParticipant);
            String pronounceName = pronounceNames.get(selectedParticipant);
            String quip = quips.get(selectedParticipant);
            
            boolean warned = false;
            nextTriggered = false;

            long maxDuration = remainingDurationMs / participants.size();
            view.showText("Go " + participantName + " for " + maxDuration
                    / 1000 + " seconds...");
            timer.startTimer();
            
            say(pronounceName + " proceed!");
            say(quip);
            
            long personDuration = timer.getMs();

            while ((personDuration < maxDuration) && (!nextTriggered)) {
                personDuration = timer.getMs();

                long remainingForPerson = maxDuration - timer.getMs();

                if ((remainingForPerson < 30 * 1000) && (!warned)) {
                    view.showText("Finish up " + participantName
                            + "!  Less than 30 seconds left!");
                    
                    say("Hurry up " + pronounceName + "!  You have 30 seconds!");
                    
                    warned = true;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            say(pronounceName + " finished!");
            view.showText(participantName + " done!");
            timer.stopTimer();
            timer.reset();

            participants.remove(selectedParticipant);
            pronounceNames.remove(selectedParticipant);
            quips.remove(selectedParticipant);
            remainingDurationMs -= personDuration;

        }

        say("Congratulations, scrum completed successfully!");
        view.showText("Scrum complete!  Press space to restart...");
        timer.terminate();

        scrumming = false;
    }

    public static void main(String[] args) {

        // Schedule a job for event dispatch thread:
        // creating and showing this application's GUI.
        final ScrumGM scrummer = new ScrumGM();
        scrummer.start();
        scrummer.destroy();
    }

    public void startTriggered() {
        if (!scrumming) {
            scrumTriggered = true;
        }
    }

    public void nextTriggered() {
        nextTriggered = true;
    }
}

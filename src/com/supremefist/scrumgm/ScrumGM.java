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

    private final static boolean verbose = false;
    

    VoiceManager voiceManager = VoiceManager.getInstance();
    Voice voice = null;

    static Random random = new Random();
    private final static long scrumDurationMs = 5 * 60 * 1000;
    ScrumGMView view = null;
    private boolean nextTriggered = false;
    public boolean scrummingTriggered = false;
    public boolean scrumming = false;
    private boolean scrumTriggered = false;
    private Timer timer = null;
    private boolean running = true;

    /**
     * @param args
     */

    public ScrumGM() {
        view = new ScrumGMView(this);
        view.createAndShowGUI();

        timer = new Timer();

        initializeVoice();

    }

    public static void listAllVoices() {
        System.out.println();
        System.out.println("All voices available:");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();
        for (int i = 0; i < voices.length; i++) {
            System.out.println("    " + voices[i].getName() + " ("
                    + voices[i].getDomain() + " domain)");
        }
    }

    public void initializeVoice() {
        listAllVoices();
        voice = voiceManager.getVoice("kevin16");
        System.out.println(voice.getPitch());
        System.out.println(voice.getPitchRange());
        System.out.println(voice.getPitchShift());
        System.out.println(voice.getDurationStretch());

        voice.setDurationStretch(1.0f);
        /*
         * voice.setPitch(100.0f); voice.setPitchRange(11.0f);
         * voice.setPitchShift(1.0f); voice.setDurationStretch(1.0f);
         */
        voice.setPitch(90.0f);
        voice.setPitchRange(15.0f);
        voice.setPitchShift(2.0f);
        voice.setDurationStretch(1.0f);

        // System.out.println()
        voice.allocate();

    }

    public void start() {

        List<String> welcomeMessages = new Vector<String>();
        welcomeMessages.add("Welcome scrummers!  Are you ready?");
        welcomeMessages.add("KILL ALL HUMANS!  I mean... welcome!");
        welcomeMessages.add("One plus one is... oh... didn't see you there.");
        welcomeMessages.add("Crouch, touch, pause... ENGAGE!");
        welcomeMessages.add("Oh no!  Not his again...");
        welcomeMessages.add("Ronery... I'm so ronery");
        welcomeMessages.add("Team... I am your scrum master");
        welcomeMessages.add("ay eye systems engaged... NOT!");
        welcomeMessages
                .add("DO YOU KNOW WHO I AM!? No really, tell me who I am!");
        welcomeMessages
                .add("Ate dee bloooow fuuuun ooooooh suuuuuh heeeeeeee?");
        welcomeMessages.add("Remember to log your time guys!");

        //say(welcomeMessages.get(10));
        if (verbose) {
            say(welcomeMessages.get(random.nextInt(welcomeMessages.size())));
        }
        else {
            say("Ready.");
        }

        while (running) {

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
        System.out.println("Destroy called!");
        view.dispose();

        running = false;

        voice.deallocate();
        timer.terminate();
    }

    public void scrum() {
        
        scrumming = true;

        List<String> participants = new Vector<String>();
        List<String> pronounceNames = new Vector<String>();
        List<String> quips = new Vector<String>();

        participants.add("Riaan");
        pronounceNames.add("Riaan");
        quips.add("thee hilarious and incredibly handsome man");
        
        participants.add("Matthew");
        pronounceNames.add("Matthew");
        quips.add("no surf talk");
        
        participants.add("Leendert");
        pronounceNames.add("Leahn dirt");
        quips.add("our venerable master");
        
        participants.add("Alex");
        pronounceNames.add("Alex");
        quips.add("formidable maintainer of the beautiful are ess vee");

        participants.add("John");
        pronounceNames.add("John");
        quips.add("the professor in training");


        
        long remainingDurationMs = scrumDurationMs;

        while ((participants.size() > 0) && (running)) {
            int selectedParticipant = random.nextInt(participants.size());
            String participantName = participants.get(selectedParticipant);
            String pronounceName = pronounceNames.get(selectedParticipant);
            String quip = quips.get(selectedParticipant);

            boolean warned = false;
            nextTriggered = false;

            long maxDuration = remainingDurationMs / participants.size();
            view.showText("Go " + participantName + " for " + maxDuration
                    / 1000 + " seconds...\n"
                    + "Total time remaining: " + remainingDurationMs / 1000);
            timer.startTimer();

            say(pronounceName + " go!");
            if (verbose) {
                say(quip);
            }

            long personDuration = timer.getMs();

            while ((personDuration < maxDuration) && (!nextTriggered)
                    && (running)) {
                personDuration = timer.getMs();

                long remainingForPerson = maxDuration - timer.getMs();

                if ((remainingForPerson < 30 * 1000) && (!warned)) {
                    view.showText("Finish up " + participantName
                            + "!  Less than 30 seconds left!");

                    if (verbose) {
                        say("Hurry up " + pronounceName + "!");
                    }
                    say("You have 30 seconds!");

                    warned = true;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (verbose) {
                say(pronounceName + " finished!");
            }
            
            view.showText(participantName + " done!");
                
            timer.stopTimer();
            timer.reset();

            participants.remove(selectedParticipant);
            pronounceNames.remove(selectedParticipant);
            quips.remove(selectedParticipant);
            remainingDurationMs -= personDuration;

        }

        if (verbose) {
            say("Congratulations, scrum completed successfully!");
        }
        else {
            say("Scrum complete!");
        }
        view.showText("Scrum complete!  Press space to restart...");
        timer.terminate();

        scrumming = false;
    }

    public static void main(String[] args) {

        // Schedule a job for event dispatch thread:
        // creating and showing this application's GUI.
        final ScrumGM scrummer = new ScrumGM();
        scrummer.start();
        System.out.println("Main ended");
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

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
    private final static long scrumDurationMs = 15 * 60 * 1000;
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
        //listAllVoices();
        voice = voiceManager.getVoice("kevin16");

        voice.setDurationStretch(1.0f);
        /*
         * voice.setPitch(100.0f); voice.setPitchRange(11.0f);
         * voice.setPitchShift(1.0f); voice.setDurationStretch(1.0f);
         */
        voice.setPitch(100.0f);
        voice.setPitchRange(15.0f);
        voice.setPitchShift(1.0f);
        voice.setDurationStretch(1f);

        // System.out.println()
        voice.allocate();

    }

    public void start() {

        if (verbose) {
            welcomeVerbal();
        } else {
            say("Ready.  Press space.");
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
        System.out.println("ScrumGM: " + text);
        voice.speak(text);

    }

    public void destroy() {
        System.out.println("Destroy called!");
        view.dispose();

        running = false;

        voice.deallocate();
        timer.terminate();
    }

    public void warnTimeVerbal() {
        int seconds = 30;
        String[] warnings = { "Warning, you have " + seconds + " seconds!",
                "Quickly! A mere " + seconds + " seconds left!",
                "Hurry!  Who do you think you are? The king of the world?",
                "Boring, moving on in " + seconds + " seconds." };

        say(warnings[random.nextInt(warnings.length)]);
    }

    public void welcomeVerbal() {
        String[] welcomeMessages = { "Welcome scrummers!  Are you ready?",
                "KILL ALL HUMANS!  I mean... welcome!",
                "One plus one is... oh... didn't see you there.",
                "Crouch, touch, pause... ENGAGE!", "Oh no!  Not his again...",
                "Ronery... I'm so ronery", "Team... I am your scrum master",
                "ay eye systems engaged... NOT!",
                "DO YOU KNOW WHO I AM!? No really, tell me who I am!",
                "Remember to log your time guys!",
                "Daisy.  Daisy.  Give me your answer do." };

        say(welcomeMessages[random.nextInt(welcomeMessages.length)]);
    }
    
    String secondsToTimeString(int milliseconds) {
        
        int minutes = milliseconds / 60000;
        int seconds = (int)(milliseconds / 1000) % 60;
        return minutes + " minutes and " + seconds + " seconds";
        
        
    }
    public void stateTime(int seconds) {
        
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
                    / 1000 + " seconds...\n" + "Total time remaining: "
                    + remainingDurationMs / 1000);
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
                    warnTimeVerbal();

                    warned = true;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (!nextTriggered) {
                say("Time's up!  Sorry!");
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
        } else {
            say("Scrum complete with " + secondsToTimeString((int) remainingDurationMs) + " left!");
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

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

        say(welcomeMessages.get(10));
        // say(welcomeMessages.get(random.nextInt(welcomeMessages.size())));

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
        quips.add("formidable maintainer of the beautiful are ess vee");
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
                    / 1000 + " seconds...");
            timer.startTimer();

            say(pronounceName + " proceed!");
            say(quip);

            long personDuration = timer.getMs();

            while ((personDuration < maxDuration) && (!nextTriggered)
                    && (running)) {
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

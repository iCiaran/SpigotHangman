package uk.co.ciaranmoran.hangman;

import java.util.ArrayList;

public class Game {

    private boolean inProgress;
    private ArrayList<Character> guesses;
    private ArrayList<Character> word;
    private ArrayList<Character> censored;
    private int numberGuesses;
    private int numberFailed;

    private static Game instance;

    private Game() {
        inProgress = false;
    }

    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void startGame(String s) {

        this.guesses = new ArrayList<>();
        this.censored = new ArrayList<>();
        this.word = new ArrayList<>();

        this.numberFailed = 0;
        this.numberGuesses = 0;

        for (char c : s.toCharArray()) {
            c = Character.toLowerCase(c);
            if (Character.isAlphabetic(c)) {
                this.word.add(c);
                this.censored.add('_');
            } else if (Character.isWhitespace(c)) {
                this.word.add('/');
                this.censored.add('/');
            }
        }
        this.setInProgress(true);
        System.out.println(word);
        System.out.println(censored);
    }

    public void stopGame() {
        this.setInProgress(false);
    }

    public void makeGuess(String s) {

    }
}

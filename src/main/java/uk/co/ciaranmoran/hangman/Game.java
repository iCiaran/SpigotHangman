package uk.co.ciaranmoran.hangman;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Game {

    private boolean inProgress;
    private Set<Character> guesses;
    private ArrayList<Character> word;
    private ArrayList<Character> censored;
    private int numberFailed;

    private final int MAX_GUESSES = 10;

    private static Game instance;

    private Game() {
        inProgress = false;
    }

    static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    boolean isInProgress() {
        return inProgress;
    }

    String getCensored() {
        return this.censored.toString();
    }

    String getGuessed() {
        return this.guesses.toString();
    }

    int getRemaining() {
        return this.MAX_GUESSES - this.numberFailed;
    }

    void startGame(String s) {

        this.guesses = new HashSet<>();
        this.censored = new ArrayList<>();
        this.word = new ArrayList<>();

        this.numberFailed = 0;

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
    }

    void stopGame() {
        this.setInProgress(false);
    }

    void makeGuess(CommandSender commandSender, String s) {

        if (!this.isInProgress()) {
            commandSender.sendMessage("There isn't a game in progress right now!");
            return;
        }

        if (s.length() == 1 ) {
            if(!Character.isAlphabetic(s.charAt(0))){
                commandSender.sendMessage("You must guess a letter!");
                return;
            }
            this.guessLetter(commandSender, Character.toLowerCase(s.charAt(0)));
        } else {
            this.guessWord(commandSender, s);
        }

        if (this.checkLose()) {
            commandSender.sendMessage("Game Over!");
            this.stopGame();
        }

        if (this.checkWin()) {
            commandSender.sendMessage("Congratulations You Won!");
            this.stopGame();
        }
    }

    private boolean checkWin() {
        for (char c : this.censored) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    private boolean checkLose() {
        return (numberFailed >= MAX_GUESSES);
    }

    private void guessLetter(CommandSender commandSender, char c) {
        if (this.guesses.contains(c)) {
            commandSender.sendMessage("That letter has already been guessed!");
            return;
        }

        this.guesses.add(c);

        boolean found = false;

        for (int i = 0; i < this.word.size(); i++) {
            if (this.censored.get(i) != '/' && this.word.get(i) == c) {
                this.censored.set(i, this.word.get(i));
                found = true;
            }
        }

        if (found) {
            commandSender.sendMessage("Well done, the word is now: ");
            commandSender.sendMessage(this.getCensored());
        } else {
            commandSender.sendMessage("Letter " + c + " was not found!");
            this.numberFailed++;
        }
    }

    private void guessWord(CommandSender commandSender, String s) {
        commandSender.sendMessage("Guessing whole words is not implemented yet, oops!");
        //TODO
    }
}

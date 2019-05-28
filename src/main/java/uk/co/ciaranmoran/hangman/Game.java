package uk.co.ciaranmoran.hangman;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Game {

    private boolean inProgress;
    private Set<Character> guesses;
    private ArrayList<Character> word;
    private ArrayList<Character> censored;
    private int numberFailed;
    private Set<CommandSender> players;

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

    private String getCensored() {
        return this.censored.toString();
    }

    private String getGuessed() {
        return this.guesses.toString();
    }

    private int getRemaining() {
        return this.MAX_GUESSES - this.numberFailed;
    }

    void startGame(CommandSender commandSender, String s) {
        this.guesses = new HashSet<>();
        this.censored = new ArrayList<>();
        this.word = new ArrayList<>();
        this.players = new HashSet<>();

        this.addToPlayers(commandSender);

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

    private void stopGame() {
        this.setInProgress(false);
    }

    void stop() {
        this.sendToAll("Stopping the game");
        this.stopGame();
    }

    private void addToPlayers(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            this.players.add(commandSender);
        }
    }

    void makeGuess(CommandSender commandSender, String s) {
        this.addToPlayers(commandSender);
        if (!this.isInProgress()) {
            this.sendMessage(commandSender, "There isn't a game in progress right now!");
            return;
        }

        if (s.length() == 1) {
            if (!Character.isAlphabetic(s.charAt(0))) {
                this.sendMessage(commandSender, "You must guess a letter!");
                return;
            }
            this.guessLetter(commandSender, Character.toLowerCase(s.charAt(0)));
        } else {
            this.guessWord(commandSender, s);
        }

        if (this.checkLose()) {
            this.sendToAll("Game Over!");
            this.stopGame();
        }

        if (this.checkWin()) {
            this.sendToAll("Congratulations You Won!");
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
            this.sendMessage(commandSender, "That letter has already been guessed!");
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
            this.sendToAll(commandSender.getName() + " correctly guessed the letter: " + c + "!");
        } else {
            this.sendToAll(commandSender.getName() + " guessed: " + c + ", but it wasn't found!");
            this.numberFailed++;
        }
        this.sendToAll(this.getStatus());
    }

    private void guessWord(CommandSender commandSender, String s) {
        this.sendMessage(commandSender, "Guessing whole words is not implemented yet, oops!");
        //TODO
    }

    private void sendToAll(String message) {
        for (CommandSender commandSender : players) {
            this.sendMessage(commandSender, message);
        }
    }

    private void sendToAll(String[] messages) {
        for (String message : messages) {
            sendToAll(message);
        }
    }

    String[] getStatus() {
        if (!this.isInProgress()) {
            return new String[]{"There is no game in progress right now!"};
        } else {
            return new String[]{"Phrase: " + Game.getInstance().getCensored(),
                    "Guessed: " + Game.getInstance().getGuessed(),
                    "Guesses remaining: " + Game.getInstance().getRemaining()};
        }
    }

    private void sendMessage(CommandSender commandSender, String message) {
        String prefix = "[Hangman] ";
        commandSender.sendMessage(prefix + message);
    }

}

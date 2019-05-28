package uk.co.ciaranmoran.hangman;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CommandHangman implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        switch (args[0]) {
            case "start":
                return start(commandSender, args);
            case "status":
                return status(commandSender);
            case "stop":
                return stop(commandSender);
            case "guess":
                return guess(commandSender, args);
        }
        return false;
    }

    private boolean start(CommandSender commandSender, String[] args) {
        if (Game.getInstance().isInProgress()) {
            commandSender.sendMessage("There is already a game in progress");
        } else if (args.length < 2) {
            commandSender.sendMessage("Usage: /hangman start <word>");
            return false;
        } else {
            Game.getInstance().startGame(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
            commandSender.sendMessage("Game Started!");
        }

        return true;
    }

    private boolean status(CommandSender commandSender) {
        boolean inProgress = Game.getInstance().isInProgress();
        commandSender.sendMessage("Game in progress: " + inProgress);
        if (inProgress) {
            commandSender.sendMessage("Hint: " + Game.getInstance().getCensored());
            commandSender.sendMessage("Guessed: " + Game.getInstance().getGuessed());
            commandSender.sendMessage("Guesses remaining: " + Game.getInstance().getRemaining());
        }
        return true;
    }

    private boolean stop(CommandSender commandSender) {
        Game.getInstance().stopGame();
        commandSender.sendMessage("Stopping Game!");
        return true;
    }

    private boolean guess(CommandSender commandSender, String[] args) {
        if(args.length < 2) {
            commandSender.sendMessage("Usage: /hangman guess <letter/word>");
            return false;
        }
        Game.getInstance().makeGuess(commandSender, args[1]);
        return true;
    }

}

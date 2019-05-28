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
            case "source":
                return source(commandSender);
        }
        return false;
    }

    private boolean start(CommandSender commandSender, String[] args) {
        if (Game.getInstance().isInProgress()) {
            commandSender.sendMessage("There is already a game in progress");
        } else if (args.length < 2) {
            return false;
        } else {
            Game.getInstance().startGame(commandSender, StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
            commandSender.getServer().broadcastMessage("[Hangman] " + commandSender.getName() + " has started a game of hangman!");
        }

        return true;
    }

    private boolean status(CommandSender commandSender) {
        commandSender.sendMessage(Game.getInstance().getStatus());
        return true;
    }

    private boolean stop(CommandSender commandSender) {
        Game.getInstance().stop();
        commandSender.sendMessage("You have stopped the game.");
        return true;
    }

    private boolean guess(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        Game.getInstance().makeGuess(commandSender, args[1]);
        return true;
    }

    private boolean source(CommandSender commandSender) {
        commandSender.sendMessage("https://github.com/iCiaran/SpigotHangman");
        return true;
    }

}

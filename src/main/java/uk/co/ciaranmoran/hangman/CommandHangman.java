package uk.co.ciaranmoran.hangman;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.Arrays;

public class CommandHangman implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "start":
                return start(commandSender, args);
            case "status":
                return status(commandSender, args);
            case "stop":
                return stop(commandSender, args);
            case "guess":
                return guess(commandSender, args);
        }
        return false;
    }

    public boolean start(CommandSender commandSender, String[] args) {
        if (Game.getInstance().isInProgress()) {
            commandSender.sendMessage("There is already a game in progress");
        } else if (args.length < 2) {
            commandSender.sendMessage("Usage: /hangman start <word>");
        } else {
            Game.getInstance().startGame(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
            commandSender.sendMessage("Game Started!");
        }

        return true;
    }

    public boolean status(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("Game in progress: " + Game.getInstance().isInProgress());
        return true;
    }

    public boolean stop(CommandSender commandSender, String[] args) {
        Game.getInstance().stopGame();
        return true;
    }

    public boolean guess(CommandSender commandSender, String[] args) {
        Game.getInstance().makeGuess(args[1]);
        return true;
    }

}

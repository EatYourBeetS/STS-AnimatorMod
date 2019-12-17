package eatyourbeets.console;

import basemod.devcommands.ConsoleCommand;
import eatyourbeets.console.commands.CreateCustomCard;

public abstract class CommandsManager
{
    public static void RegisterCommands()
    {
        ConsoleCommand.addCommand("createcustomcard", CreateCustomCard.class);
    }
}

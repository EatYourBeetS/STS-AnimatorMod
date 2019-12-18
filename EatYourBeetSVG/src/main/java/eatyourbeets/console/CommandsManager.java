package eatyourbeets.console;

import basemod.devcommands.ConsoleCommand;
import eatyourbeets.console.commands.CreateCustomCard;

import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class CommandsManager
{
    public static void RegisterCommands()
    {
        if (Files.exists(Paths.get("C:/temp/Animator-DynamicCards.json")))
        {
            ConsoleCommand.addCommand("createcustomcard", CreateCustomCard.class);
        }
    }
}

 package eatyourbeets.console.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.AnimatorCard_Dynamic;
import eatyourbeets.console.DynamicCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateCustomCard extends ConsoleCommand
{
    private static final Type typeToken = new TypeToken<HashMap<String, DynamicCardData>>(){}.getType();
    private static final Map<String, DynamicCardData> cardPool = new HashMap<>();

    public CreateCustomCard()
    {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth)
    {
        if (AbstractDungeon.isScreenUp)
        {
            DevConsole.log("Could not create a custom card.");
            return;
        }

        String key = tokens[depth];
        DynamicCardData data = cardPool.get(key);
        try
        {
            AnimatorCard_Dynamic card = data.GenerateCard(key, cardPool);
            AnimatorCard_Dynamic upgraded = (AnimatorCard_Dynamic)card.makeCopy();

            upgraded.upgrade();

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.addToTop(card);
            group.addToTop(upgraded);

            GameActions.Bottom.SelectFromPile(card.name, 999, group)
            .SetOptions(false, true)
            .AddCallback(cards ->
            {
                if (DevConsole.priorCommands.size() > 0)
                {
                    DevConsole.priorCommands.remove(DevConsole.priorCommands.size() - 1);
                }

                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.MakeCardInHand(c);
                }
            });
        }
        catch (Exception ex)
        {
            DevConsole.log("Could not create a custom card.");
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth)
    {
        if (depth >= 2)
        {
            complete = true;

            return new ArrayList<>();
        }

        if (depth == 1 && tokens[1].equals(""))
        {
            JavaUtilities.GetLogger(getClass()).info("Generating Card Pool");

            GenerateCardPool();
        }

        complete = false;

        return new ArrayList<>(cardPool.keySet());
    }

    private static void GenerateCardPool()
    {
        cardPool.clear();

        String jsonString;
        try
        {
            jsonString = new String(Files.readAllBytes(Paths.get("C:/temp/Animator-DynamicCards.json")));
            cardPool.putAll(new Gson().fromJson(jsonString, typeToken));
        }
        catch (Exception e)
        {
            DevConsole.log("Could not generate card pool.");
            e.printStackTrace();
        }
    }
}

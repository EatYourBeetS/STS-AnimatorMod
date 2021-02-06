package eatyourbeets.console.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.CustomCardLibSortHeader;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class ParseGenericCommand extends ConsoleCommand
{
    private static Object temp;

    public ParseGenericCommand()
    {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 99;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth)
    {
        try
        {
            if (tokens.length > 1)
            {
                if (tokens[1].equals("starter") && tokens.length > 2)
                {
                    String loadoutName = tokens[2].replace("_", " ");
                    AnimatorLoadout loadout = GR.Animator.Data.GetByName(loadoutName);
                    if (GameUtilities.InGame() && loadout != null && player != null && player.masterDeck != null)
                    {
                        player.masterDeck.group.removeAll(player.masterDeck.getPurgeableCards().group);

                        for (String cardID : loadout.GetStartingDeck())
                        {
                            player.masterDeck.group.add(CardLibrary.getCard(cardID).makeCopy());
                        }

                        DevConsole.log("Replaced starting deck with: " + loadoutName);
                    }
                    else
                    {
                        DevConsole.log("Error processing command.");
                    }

                    return;
                }

                if (tokens[1].equals("stance") && tokens.length > 2)
                {
                    AbstractStance stance = AbstractStance.getStanceFromName(tokens[2]);
                    if (stance == null)
                    {
                        stance = AbstractStance.getStanceFromName(GR.Common.CreateID(tokens[2] + "Stance"));
                    }

                    if (stance != null)
                    {
                        GameActions.Bottom.ChangeStance(stance);
                    }
                    else
                    {
                        DevConsole.log("Stance not found.");
                    }
                    return;
                }

                if (tokens[1].equals("sort-by-tribe"))
                {
                    CustomCardLibSortHeader.Instance.group.group.sort((a, b) ->
                    {
                        int aValue = 0;
                        if (a.hasTag(AnimatorCard.SPELLCASTER))
                        {
                            aValue = 1;
                        }
                        else if (a.hasTag(AnimatorCard.MARTIAL_ARTIST))
                        {
                            aValue = 2;
                        }
                        else if (a.hasTag(AnimatorCard.SHAPESHIFTER))
                        {
                            aValue = 3;
                        }

                        int bValue = 0;
                        if (b.hasTag(AnimatorCard.SPELLCASTER))
                        {
                            bValue = 1;
                        }
                        else if (b.hasTag(AnimatorCard.MARTIAL_ARTIST))
                        {
                            bValue = 2;
                        }
                        else if (b.hasTag(AnimatorCard.SHAPESHIFTER))
                        {
                            bValue = 3;
                        }

                        return Integer.compare(aValue, bValue);
                    });
                    return;
                }

                if (tokens[1].equals("set-zoom"))
                {
                    GR.Animator.Config.CropCardImages(tokens.length > 2 && tokens[2].equals("true"), true);
                    return;
                }

                if (tokens[1].equals("get-cards") && tokens.length > 2)
                {
                    if (!GameUtilities.InGame() || player == null || player.masterDeck == null)
                    {
                        DevConsole.log("You need to be in game to use this command.");
                        return;
                    }

                    temp = tokens[2].replace("_", " ");
                    ArrayList<AnimatorCard> cards = new ArrayList<>();
                    Synergy synergy = JUtils.Find(Synergies.GetAll(), s -> s.Name.equals(temp));
                    if (synergy != null)
                    {
                        Settings.seedSet = true;
                        player.masterDeck.clear();
                        Synergies.AddCards(synergy, CardLibrary.getAllCards(), cards);

                        cards.sort(new CardRarityComparator());

                        for (AnimatorCard card : cards)
                        {
                            AbstractCard temp = card.makeCopy();
                            player.masterDeck.group.add(temp);
                            if (temp.canUpgrade())
                            {
                                temp = card.makeCopy();
                                temp.upgrade();
                                player.masterDeck.group.add(temp);
                            }
                        }
                    }

                    DevConsole.log("Found " + cards.size() + " cards.");
                    return;
                }

                if (tokens[1].equals("show-upgrades"))
                {
                    SingleCardViewPopup.isViewingUpgrade ^= true;
                    return;
                }

                if (tokens[1].equals("crop"))
                {
                    DevConsole.log("This command is currently not available.");
                    return;
                }

                if (tokens[1].equals("show-special"))
                {
                    CustomCardLibSortHeader.ShowSpecial = tokens.length > 2 && tokens[2].equals("true");
                    return;
                }

                if (tokens[1].equals("unlock-all-cards"))
                {
                    for (AbstractCard c : CardLibrary.getAllCards())
                    {
                        UnlockTracker.unlockCard(c.cardID);
                        UnlockTracker.markCardAsSeen(c.cardID);
                    }
                    return;
                }

                if (tokens[1].equals("remove-colorless"))
                {
                    final FieldInfo<CardGroup> _colorless = JUtils.GetField("colorlessCards", CardLibraryScreen.class);
                    if (CustomCardLibSortHeader.Screen != null)
                    {
                        _colorless.Get(CustomCardLibSortHeader.Screen).group.removeIf(card -> card.rarity == AbstractCard.CardRarity.BASIC || !(card instanceof EYBCard));
                    }

                    DevConsole.log("Removed cards from colorless library.");
                    return;
                }

                Test(tokens);
            }
        }
        catch (Exception ex)
        {
            DevConsole.log("Error: " + ex.getClass().getSimpleName());
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth)
    {
        ArrayList<String> suggestions = new ArrayList<>();

        if (tokens.length > 1 && tokens.length <= 3)
        {
            if (tokens[1].equals("starter") || tokens[1].equals("get-cards"))
            {
                for (AnimatorLoadout loadout : GR.Animator.Data.GetEveryLoadout())
                {
                    suggestions.add(loadout.Name.replace(" ", "_"));
                }
            }
        }

        return suggestions;
    }

    public static void Test(String[] tokens) throws NumberFormatException
    {
        Float[] values = new Float[tokens.length - 1];
        for (int i = 0; i < values.length; i++)
        {
            values[i] = Float.parseFloat(tokens[i + 1]);
        }

        Testing.SetValues(values);
    }
}

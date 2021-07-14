package eatyourbeets.console.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.auras.Aura;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.CustomCardLibSortHeader;
import eatyourbeets.utilities.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
                if (tokens[1].equals("ghost"))
                {
                    player.tint.color.a = (tokens.length > 2 ? JUtils.ParseFloat(tokens[2], 1) : 0.3f);

                    return;
                }

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

                if (tokens[1].equals("add-score") && tokens.length > 2)
                {
                    UnlockTracker.addScore(GR.Animator.PlayerClass, JUtils.ParseInt(tokens[2], 0));

                    return;
                }

                if (tokens[1].equals("unlock-level") && tokens.length > 2)
                {
                    int level = Math.max(0, Math.min(GR.Animator.Data.MaxUnlockLevel, JUtils.ParseInt(tokens[2], 5)));
                    if (UnlockTracker.getUnlockLevel(GR.Animator.PlayerClass) > level)
                    {
                        UnlockTracker.resetUnlockProgress(GR.Animator.PlayerClass);
                    }

                    while (UnlockTracker.getUnlockLevel(GR.Animator.PlayerClass) < level)
                    {
                        UnlockTracker.addScore(GR.Animator.PlayerClass, 500);
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

                if (tokens[1].equals("sort-by-class"))
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

                if (tokens[1].equals("set-synergy-check"))
                {
                    GR.Animator.Config.FadeCardsWithoutSynergy.Set(tokens.length > 2 && tokens[2].equals("true"), true);
                    return;
                }

                if (tokens[1].equals("set-zoom"))
                {
                    GR.Animator.Config.CropCardImages.Set(tokens.length > 2 && tokens[2].equals("true"), true);
                    return;
                }

                if (tokens[1].equals("set-simple-ui"))
                {
                    GR.Animator.Config.SimplifyCardUI.Set(tokens.length > 2 && tokens[2].equals("true"), true);
                    return;
                }

                if (tokens[1].equals("extract-card-data"))
                {
                    Map<String, Map<String, EYBCardMetadataV2>> data = new HashMap<>();
                    ExtractCardData(data, CardLibrary.cards.values());
                    ExtractCardData(data, AnimatorCard_UltraRare.GetCards().values());
                    ExtractCardData(data, Aura.GetCards());

                    String filePath = "C://temp//" + ((tokens.length > 2) ? tokens[2] : "Animator-CardMetadata") + ".json";
                    new Gson().toJson(data, new FileWriter(filePath));
                    DevConsole.log("Exported metadata to: " + filePath);

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
                    CardSeries series = JUtils.Find(CardSeries.GetAllSynergies(), s -> s.Name.equals(temp));
                    if (series != null)
                    {
                        Settings.seedSet = true;
                        player.masterDeck.clear();
                        CardSeries.AddCards(series, CardLibrary.getAllCards(), cards);

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
                        String key = c.cardID;
                        //UnlockTracker.unlockCard(), without flushing after every card.
                        UnlockTracker.seenPref.putInteger(key, 1);
                        UnlockTracker.unlockPref.putInteger(key, 2);
                        UnlockTracker.lockedCards.remove(key);
                        AbstractCard card = CardLibrary.getCard(key);
                        if (card != null)
                        {
                            card.isSeen = true;
                            card.unlock();
                        }
                    }

                    UnlockTracker.unlockPref.flush();
                    UnlockTracker.seenPref.flush();
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
                    suggestions.add(loadout.Name);//.replace(" ", "_"));
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

    private void ExtractCardData(Map<String, Map<String, EYBCardMetadataV2>> data, Collection cards)
    {
        for (Object o : cards)
        {
            AnimatorCard c = JUtils.SafeCast(o, AnimatorCard.class);
            if (c == null)
            {
                continue;
            }

            String key;
            boolean exportSeries = true;
            if (c instanceof AnimatorCard_UltraRare)
            {
                key = "ULTRARARE";
            }
            else if (c instanceof Aura)
            {
                key = "AURA";
            }
            else if (c.type == AbstractCard.CardType.STATUS)
            {
                key = "STATUS";
            }
            else if (c.type == AbstractCard.CardType.CURSE)
            {
                key = "CURSE";
            }
            else if (c.rarity == AbstractCard.CardRarity.BASIC)
            {
                key = "BASIC";
            }
            else if (c.rarity == AbstractCard.CardRarity.SPECIAL)
            {
                key = "SPECIAL";
            }
            else if (c.color == AbstractCard.CardColor.COLORLESS)
            {
                key = "COLORLESS:" + (c.rarity == AbstractCard.CardRarity.UNCOMMON ? "UNCOMMON" : "RARE");
            }
            else
            {
                key = "SERIES:" + c.series.LocalizedName;
                exportSeries = false;
            }

            data
            .computeIfAbsent(key, k -> new HashMap<>())
            .put(c.cardID, EYBCardMetadataV2.Export(c, exportSeries));
        }
    }
}

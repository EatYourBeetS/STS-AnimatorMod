package eatyourbeets.console.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.actions.damage.DamageHelper;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.animator.curse.Curse_AscendersBane;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.common.CustomCardLibSortHeader;
import eatyourbeets.utilities.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class ParseGenericCommand extends ConsoleCommand
{
    private AbstractMonster enemy;

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

                if (tokens[1].equals("vfx"))
                {
                    final String key = tokens[2].toUpperCase();
                    try
                    {
                        final FieldInfo<AbstractGameAction.AttackEffect> effectField = JUtils.GetField(key, AttackEffects.class);
                        final AbstractGameAction.AttackEffect effect = effectField.Get(null);
                        GameActions.Bottom.WaitRealtime(0.3f);
                        GameActions.Bottom.Callback(effect, (vfx, __) ->
                        {
                            enemy = GameUtilities.GetRandomEnemy(true);
                            GameEffects.List.Attack(player, enemy, vfx, 1, 1);
                            final float delay = AttackEffects.GetDamageDelay(vfx);
                            if (delay > 0)
                            {
                                GameActions.Bottom.WaitRealtime(delay);
                            }
                            GameActions.Bottom.Callback(vfx, (vfx2, ___) ->
                            {
                                DamageHelper.ApplyTint(enemy, null, vfx2);
                                enemy.useStaggerAnimation();
                            });
                        });
                    }
                    catch (Exception ignored) { }

                    return;
                }

                if (tokens[1].equals("sfx") && tokens.length > 2)
                {
                    final String key = tokens[2].toUpperCase();
                    final float p1 = tokens.length > 3 ? JUtils.ParseFloat(tokens[3], 1) : 1;
                    final float p2 = tokens.length > 4 ? JUtils.ParseFloat(tokens[4], p1) : p1;
                    SFX.Play(key, p1, p2);

                    return;
                }

                if (tokens[1].equals("bane"))
                {
                    ArrayList<AbstractCard> group = player.masterDeck.group;
                    for (int i = 0; i < group.size(); i++)
                    {
                        AbstractCard c = group.get(i);
                        if (c.cardID.equals(AscendersBane.ID) || c.cardID.equals(Curse_AscendersBane.DATA.ID))
                        {
                            c = Curse_AscendersBane.DATA.MakeCopy(false);
                            ((Curse_AscendersBane)c).AddUnnamedReignEffect();
                            group.set(i, c);
                        }
                    }

                    if (GameUtilities.InBattle())
                    {
                        GameActions.Bottom.MakeCardInHand(new Curse_AscendersBane())
                        .AddCallback(c -> ((Curse_AscendersBane)c).AddUnnamedReignEffect());
                    }

                    return;
                }

                if (tokens[1].equals("show-cards"))
                {
                    player.masterDeck.clear();

                    boolean upgrade = tokens.length > 2 && tokens[2].equals("true");
                    for (EYBCardData data : ImprovedDefend.GetCards())
                    {
                        player.masterDeck.group.add(data.CreateNewInstance(upgrade));
                    }
                    for (EYBCardData data : ImprovedStrike.GetCards())
                    {
                        player.masterDeck.group.add(data.CreateNewInstance(upgrade));
                    }
                    for (EYBCardData data : AffinityToken.GetCards())
                    {
                        player.masterDeck.group.add(data.CreateNewInstance(upgrade));
                    }
                    for (Enchantment card : Enchantment.GetCards())
                    {
                        player.masterDeck.group.add(card.makeCopy());
                        player.masterDeck.group.addAll(card.GetUpgrades());
                    }
                    return;
                }

                if (tokens[1].equals("affinity") && tokens.length > 2)
                {
                    ArrayList<AbstractCard> cards;
                    if (GameUtilities.InGame())
                    {
                        cards = player.masterDeck.group;
                        cards.clear();
                    }
                    else
                    {
                        cards = new ArrayList<>();
                    }

                    boolean upgrade = tokens.length > 3 && tokens[3].equals("+");
                    for (Affinity affinity : Affinity.All())
                    {
                        if (affinity.name().toLowerCase().equals(tokens[2]))
                        {
                            for (Map.Entry<String, AbstractCard> pair : CardLibrary.cards.entrySet())
                            {
                                AbstractCard card = pair.getValue();
                                if (!GameUtilities.IsHindrance(card) && card.rarity != AbstractCard.CardRarity.SPECIAL
                                && card.color != AbstractCard.CardColor.COLORLESS && GameUtilities.GetAffinityLevel(card, affinity, false) > 0)
                                {
                                    card = card.makeCopy();
                                    if (upgrade)
                                    {
                                        card.upgrade();
                                    }
                                    cards.add(card);
                                }
                            }
                            break;
                        }
                    }
                    DevConsole.log("Total " + tokens[2] + " cards: " + cards.size());

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

                if (tokens[1].equals("apply-enchantment") && tokens.length > 3)
                {
                    for (AbstractRelic r : player.relics)
                    {
                        if (r instanceof EnchantableRelic)
                        {
                            ((EnchantableRelic) r).ApplyEnchantment(Enchantment.GetCard(JUtils.ParseInt(tokens[2], 1), JUtils.ParseInt(tokens[3], 0)));

                            if (GameUtilities.InBattle())
                            {
                                for (int i = 0; i < player.powers.size(); i++)
                                {
                                    if (player.powers.get(i).ID.equals(r.relicId + "Power"))
                                    {
                                        player.powers.remove(i);
                                        break;
                                    }
                                }

                                r.atBattleStartPreDraw();
                            }
                        }
                    }

                    return;
                }

                if (tokens[1].equals("unlock-level") && tokens.length > 2)
                {
                    final int level = Math.max(0, Math.min(GR.Animator.Data.MaxUnlockLevel, JUtils.ParseInt(tokens[2], 5)));
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

                if (tokens[1].equals("sort-by-affinity"))
                {
                    Affinity affinity = Affinity.Star;
                    if (tokens.length > 2)
                    {
                        for (Affinity t : Affinity.All())
                        {
                            if (t.name().toLowerCase().equals(tokens[2]))
                            {
                                affinity = t;
                                break;
                            }
                        }
                    }

                    CustomCardLibSortHeader.Instance.group.group.sort(new CardAffinityComparator(affinity));
                    return;
                }

//                if (tokens[1].equals("set-synergy-check"))
//                {
//                    GR.Animator.Config.FadeCardsWithoutSynergy.Set(tokens.length > 2 && tokens[2].equals("true"), true);
//                    return;
//                }

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

                if (tokens[1].equals("extract-affinity-data"))
                {
                    final int MAX_AFFINITIES = Affinity.Basic().length;
                    final StringBuilder sb = new StringBuilder();
                    for (AnimatorLoadout loadout : GR.Animator.Data.GetEveryLoadout())
                    {
                        sb.append("**").append(loadout.Name).append("** Theme:").append(loadout.Theme).append("** Source:").append(loadout.SourceMaterial).append("**").append(" (LV1 / LV1+ / LV2)").append("\n");
                        final EYBCardAffinityStatistics stats = new AnimatorRuntimeLoadout(loadout).AffinityStatistics;
                        stats.RefreshStatistics(false, false);
                        int cards = stats.CardsCount();
                        int generalLV1 = 0;
                        int generalUP1 = 0;
                        int generalLV2 = 0;
                        for (Affinity affinity : Affinity.All())
                        {
                            final EYBCardAffinityStatistics.Group group = stats.GetGroup(affinity);
                            sb.append(":").append(TipHelper.capitalize(affinity.name())).append(":")
                            .append(" ").append(group.GetPercentageString(1))
                            .append(" / ").append(group.GetUpgradePercentageString())
                            .append(" / ").append(group.GetPercentageString(2))
                            .append("\n");
                            generalLV1 += group.Total_LV1;
                            generalUP1 += group.Total_Upgrades;
                            generalLV2 += group.Total_LV2;

                            if (affinity == Affinity.Star)
                            {
                                cards -= group.GetTotal();
                            }
                        }

                        float max = MAX_AFFINITIES * cards;
                        sb.append(":Affinity:")
                        .append(" ").append(Math.round(100 * generalLV1 / max)).append("%")
                        .append(" / ").append(Math.round(100 * generalUP1 / max)).append("%")
                        .append(" / ").append(Math.round(100 * generalLV2 / max)).append("%")
                        .append("\n");

                        sb.append("\n");
                    }

                    final String filePath = "C://temp//" + ((tokens.length > 2) ? tokens[2] : "Animator-AffinityMetadata") + ".json";
                    final FileWriter writer = new FileWriter(filePath);
                    writer.write(sb.toString());
                    writer.flush();
                    DevConsole.log("Exported metadata to: " + filePath);

                    return;
                }

                if (tokens[1].equals("extract-card-data"))
                {
                    Map<String, Map<String, EYBCardMetadataV2>> data = new HashMap<>();
                    ExtractCardData(data, CardLibrary.cards.values());
                    ExtractCardData(data, AnimatorCard_UltraRare.GetCards().values());
                    ExtractCardData(data, Enchantment.GetCards());

                    String filePath = "C://temp//" + ((tokens.length > 2) ? tokens[2] : "Animator-CardMetadata") + ".json";
                    new Gson().toJson(data, new FileWriter(filePath));
                    DevConsole.log("Exported metadata to: " + filePath);

                    return;
                }

                if (tokens[1].equals("get-random-cards"))
                {
                    if (!GameUtilities.InGame() || player == null || player.masterDeck == null)
                    {
                        DevConsole.log("You need to be in game to use this command.");
                        return;
                    }

                    player.masterDeck.clear();

                    final int amount = tokens.length > 2 ? JUtils.ParseInt(tokens[2], 10) : 10;
                    final ArrayList<AbstractCard> cards = GameUtilities.GetAvailableCards();
                    for (int i = 0; i < amount; i++)
                    {
                        player.masterDeck.addToTop(JUtils.Random(cards));
                    }

                    return;
                }

                if (tokens[1].equals("get-cards") && tokens.length > 2)
                {
                    if (!GameUtilities.InGame() || player == null || player.masterDeck == null)
                    {
                        DevConsole.log("You need to be in game to use this command.");
                        return;
                    }

                    final String name = tokens[2].replace("_", " ");
                    final ArrayList<AnimatorCard> cards = new ArrayList<>();
                    if (name.equals("colorless"))
                    {
                        cards.addAll(CardSeries.GetColorlessCards());
                    }
                    else
                    {
                        final CardSeries series = CardSeries.GetByName(name, false);
                        if (series != null)
                        {
                            CardSeries.AddCards(series, CardLibrary.getAllCards(), cards);
                        }
                    }

                    if (cards.size() > 0)
                    {
                        Settings.seedSet = true;
                        player.masterDeck.clear();
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
        final ArrayList<String> suggestions = new ArrayList<>();
        if (tokens.length > 1 && tokens.length <= 3)
        {
            if (tokens[1].equals("starter"))
            {
                for (AnimatorLoadout loadout : GR.Animator.Data.GetEveryLoadout())
                {
                    suggestions.add(loadout.Name);
                }
            }
            if (tokens[1].equals("get-cards"))
            {
                for (CardSeries series : CardSeries.GetAllSeries())
                {
                    suggestions.add(series.Name);
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
            else if (c instanceof Enchantment)
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

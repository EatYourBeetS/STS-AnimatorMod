package eatyourbeets.resources.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Pantograph;
import eatyourbeets.blights.animator.UltimateCrystalBlight;
import eatyourbeets.blights.animator.UltimateCubeBlight;
import eatyourbeets.blights.animator.UltimateWispBlight;
import eatyourbeets.cards.animator.curse.special.Curse_AscendersBane;
import eatyourbeets.cards.animator.enchantments.BlightCard;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.StringJoiner;

public class AnimatorAscensionManager
{
    public static final ArrayList<AnimatorAscensionManager> LIST = new ArrayList<>();
    public static final String ASCENSION_MODIFIER = "ASCENSION_MODIFIER";
    public static final String BLIGHT_CHOICE = "BLIGHT_CHOICE";
    public static final String SMITH_DEBUFF = "SMITH_DEBUFF";
    public static final String DOUBLE_BOSS = "DOUBLE_BOSS";

    public static final int ASCENSION_20_ACT3_BOSSES = 20;
    public static final int ASCENSION_21_SMITH_DEBUFF = 21;
    public static final int ASCENSION_22_ASCENDERS_BANE = 22;
    public static final int ASCENSION_23_POTION_DAZED = 23;
    public static final int ASCENSION_24_ACT2_BLIGHTS = 24;
    public static final int ASCENSION_25_DOUBLE_BOSSES = 25;
    public static final int MAX_ASCENSION_MODIFIER = 5;

    public String PlayerClass;
    public int MaxModifier;
    public int Modifier;

    public AnimatorAscensionManager(String playerClass, int modifier, int maxModifier)
    {
        this.PlayerClass = playerClass;
        this.Modifier = modifier;
        this.MaxModifier = Math.min(MAX_ASCENSION_MODIFIER, maxModifier);
    }

    public int GetAscensionLevel()
    {
        return AbstractDungeon.isAscensionMode ? Math.max(0, Math.min(20, AbstractDungeon.ascensionLevel)) : 0;
    }

    public int GetActualAscensionLevel()
    {
        final int ascension = GetAscensionLevel();
        return ascension >= 20 ? (20 + Modifier) : ascension;
    }

    public String GetBlightChoice()
    {
        return GR.Common.Dungeon.GetString(BLIGHT_CHOICE, null);
    }

    public boolean DoubleBosses(int actNumber)
    {
        return (actNumber == 1 || actNumber == 2) && GetActualAscensionLevel() >= ASCENSION_25_DOUBLE_BOSSES;
    }

    public void OnGameStart()
    {
        final int ascension = GetAscensionLevel();
        if (ascension < 20)
        {
            return;
        }

        GR.Common.Dungeon.SetData(ASCENSION_MODIFIER, Modifier);
        SaveData(false);

        final boolean upgradedAscendersBane = (GetActualAscensionLevel() >= ASCENSION_22_ASCENDERS_BANE);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.cardID.equals(Curse_AscendersBane.DATA.ID))
            {
                final Curse_AscendersBane temp = ((Curse_AscendersBane)c);
                if (temp.EffectLevel < 2)
                {
                    temp.SetEffectLevel(upgradedAscendersBane ? 1 : 0);
                }
            }
        }
    }

    public void OnLoadGame()
    {
        final Integer modifier = GR.Common.Dungeon.GetInteger(ASCENSION_MODIFIER, null);
        if (modifier != null)
        {
            Modifier = Math.min(MaxModifier, modifier);
        }
    }

    public void OnTrueVictory()
    {
        final int ascension = GetAscensionLevel();
        if (ascension >= 20 && Modifier >= MaxModifier && MaxModifier < MAX_ASCENSION_MODIFIER)
        {
            MaxModifier += 1;
            Modifier = MaxModifier;
            SaveData(true);
        }
    }

    public void OnUsePotion()
    {
        if (GetActualAscensionLevel() >= ASCENSION_23_POTION_DAZED)
        {
            GameActions.Last.MakeCardInDrawPile(new Status_Dazed()).SetDestination(CardSelection.Top);
        }
    }

    public void OnSmith()
    {
        if (GetActualAscensionLevel() >= ASCENSION_21_SMITH_DEBUFF)
        {
            GR.Common.Dungeon.SetData(SMITH_DEBUFF, 1);
        }
    }

    public void OnBattleStart()
    {
        final int actualAscension = GetActualAscensionLevel();
        if (actualAscension >= ASCENSION_21_SMITH_DEBUFF && GR.Common.Dungeon.GetInteger(SMITH_DEBUFF, null) != null)
        {
            GameActions.Bottom.GainTemporaryStats(-1, -1, -1);
            GR.Common.Dungeon.DeleteData(SMITH_DEBUFF);
        }
        if (actualAscension >= ASCENSION_24_ACT2_BLIGHTS && AbstractDungeon.actNum == 2 && GR.Common.Dungeon.GetString(BLIGHT_CHOICE, null) == null)
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new BlightCard(new UltimateCrystalBlight()));
            group.group.add(new BlightCard(new UltimateCubeBlight()));
            group.group.add(new BlightCard(new UltimateWispBlight()));
            GameActions.Top.SelectFromPile(GR.Common.Strings.Ascension.Title("24+"), 1, group)
            .SetOptions(false, false)
            .CancellableFromPlayer(false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    final BlightCard bc = JUtils.SafeCast(c, BlightCard.class);
                    if (bc != null)
                    {
                        GR.Common.Dungeon.SetData(BLIGHT_CHOICE, bc.blight.blightID);
                        GameUtilities.ObtainBlight(c.current_x, c.current_y, bc.blight);
                        GameActions.Top.Callback(bc.blight, (blight, __) -> blight.atBattleStart());
                    }
                    else
                    {
                        throw new RuntimeException((c == null ? null : c.cardID) + " is not an instance of BlightCard");
                    }
                }
            })
            .IsCancellable(false);
        }
        if (GameUtilities.InBossRoom() && DoubleBosses(AbstractDungeon.actNum))
        {
            if (AbstractDungeon.bossList.size() >= 2)
            {
                GameUtilities.GetCurrentRoom(true).rewardAllowed = false;
            }
            else if (!GameUtilities.HasRelic(Pantograph.ID))
            {
                AbstractDungeon.player.gainGold(12);
                AbstractDungeon.player.heal(Math.min(16, AbstractDungeon.player.maxHealth / 5));
            }
        }
    }

    // STATIC:

    public static void Initialize()
    {
        LIST.clear();
        LoadData();
    }

    public static AnimatorAscensionManager Get(AbstractPlayer.PlayerClass playerClass, boolean createIfNull)
    {
        for (AnimatorAscensionManager asc : LIST)
        {
            if (playerClass.name().equals(asc.PlayerClass))
            {
                return asc;
            }
        }

        if (createIfNull)
        {
            final AnimatorAscensionManager asc = new AnimatorAscensionManager(playerClass.name(), 0, 0);
            LIST.add(asc);
            return asc;
        }

        return null;
    }

    private static void SaveData(boolean flush)
    {
        final StringJoiner sj = new StringJoiner("|");
        for (AnimatorAscensionManager asc : LIST)
        {
            sj.add(asc.PlayerClass + "@" + asc.Modifier + "@" + asc.MaxModifier);
        }

        GR.Animator.Config.AscensionLevels.Set(sj.toString(), flush);
    }

    private static void LoadData()
    {
        final String data = GR.Animator.Config.AscensionLevels.Get(null);
        final String[] characters = JUtils.SplitString("|", data);
        for (String s : characters)
        {
            final String[] values = JUtils.SplitString("@", s);
            if (values.length >= 3)
            {
                LIST.add(new AnimatorAscensionManager(values[0], JUtils.ParseInt(values[1], 0), JUtils.ParseInt(values[2], 0)));
            }
        }
    }
}

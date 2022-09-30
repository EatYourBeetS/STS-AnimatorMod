package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.common.CommonStrings;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.function.Predicate;

public abstract class EYBCardPopupAction
{
    protected static final CommonStrings.Terms terms = GR.Common.Strings.Terms;
    protected static final AnimatorStrings.SpecialActions specialActions = GR.Animator.Strings.SpecialActions;

    public String name;
    public EYBCardTooltip tooltip;

    protected EYBCard card;

    public abstract boolean CanExecute(AbstractCard card);
    public abstract void Execute();

    public void Initialize(EYBCard card)
    {
        this.card = card;
    }

    public boolean CanExecute()
    {
        return card != null && CanExecute(card);
    }

    public void Refresh()
    {

    }

    protected void SetText(String name, EYBCardTooltip tooltip)
    {
        this.name = name;
        this.tooltip = tooltip;
    }

    protected void SetText(String name, String title, String description)
    {
        this.name = name;
        this.tooltip = new EYBCardTooltip(title, description);
    }

    protected static int Count(Predicate<AbstractCard> filter)
    {
        return JUtils.Count(AbstractDungeon.player.masterDeck.group, filter);
    }

    protected static boolean IsAnimator()
    {
        return GameUtilities.IsPlayerClass(GR.Animator.PlayerClass);
    }

    protected static boolean IsRestRoom()
    {
        return GameUtilities.GetCurrentRoom() instanceof RestRoom;
    }

    protected static boolean HasCard(AbstractCard card)
    {
        return AbstractDungeon.player.masterDeck.contains(card);
    }

    protected static boolean HasCard(EYBCardData data)
    {
        return data.IsInGroup(AbstractDungeon.player.masterDeck);
    }

    protected static boolean HasMaxHp(int requiredHP)
    {
        return AbstractDungeon.player.maxHealth > requiredHP;
    }

    protected static boolean HasHp(int requiredHP)
    {
        return AbstractDungeon.player.currentHealth > requiredHP;
    }

    protected static boolean HasGold(int requiredGold)
    {
        return AbstractDungeon.player.gold >= requiredGold;
    }

    protected static boolean Remove(AbstractCard card)
    {
        return AbstractDungeon.player.masterDeck.group.remove(card);
    }

    protected static void Obtain(AbstractCard card)
    {
        GameEffects.TopLevelList.ShowAndObtain(card);
    }

    protected static EYBCard Find(EYBCardData data, boolean prioritizeUnupgraded)
    {
        AbstractCard result = null;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (data.IsCard(c))
            {
                if (!prioritizeUnupgraded)
                {
                    result = c;
                    break;
                }
                else if (result == null || result.upgraded)
                {
                    result = c;
                }
            }
        }

        return (EYBCard) result;
    }

    protected static EYBCard Replace(AbstractCard original, EYBCardData data, boolean upgraded)
    {
        return Replace(original, data.MakeCopy(upgraded));
    }

    protected static EYBCard Replace(AbstractCard original, EYBCard replacement)
    {
        final AbstractPlayer p = AbstractDungeon.player;
        final int index = p.masterDeck.group.indexOf(original);
        if (index > 0)
        {
            for (AbstractRelic r : p.relics)
            {
                r.onObtainCard(replacement);
            }
            UnlockTracker.markCardAsSeen(replacement.cardID);
            GameUtilities.CopyVisualProperties(replacement, original);
            p.masterDeck.group.set(index, replacement);
            replacement.flash();

            return replacement;
        }

        return null;
    }

    protected static void Heal(int amount)
    {
        AbstractDungeon.player.heal(amount, true);
    }

    protected static void LoseHP(int amount)
    {
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.HP_LOSS));
    }

    protected static void LoseMaxHP(int amount)
    {
        AbstractDungeon.player.decreaseMaxHealth(amount);
    }

    protected static void GainGold(int amount)
    {
        AbstractDungeon.player.gainGold(amount);
    }

    protected static void LoseGold(int amount)
    {
        AbstractDungeon.player.loseGold(amount);
    }

    protected static void Complete()
    {
        GR.UI.CardPopup.Close();
    }
}

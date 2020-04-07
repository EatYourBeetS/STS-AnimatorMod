package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameUtilities;

public class AnimatorCardCooldown
{
    private final static Color COOLDOWN_INCOMPLETE_COLOR = Settings.GREEN_TEXT_COLOR.cpy().lerp(Settings.CREAM_COLOR, 0.5f);
    private final ActionT1<AbstractMonster> onCooldownCompleted;
    private final AnimatorCard card;

    public AnimatorCardCooldown(AnimatorCard card, int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        if (card.baseSecondaryValue != 0 || card.secondaryValue != 0)
        {
            throw new RuntimeException("Do not modify secondaryValue if you are implementing AnimatorCardCooldown.");
        }
        
        card.baseSecondaryValue = card.secondaryValue = baseCooldown;
        card.upgrade_secondaryValue = cooldownUpgrade;
        this.onCooldownCompleted = onCooldownCompleted;
        this.card = card;
    }

    public ColoredString GetSecondaryValueString()
    {
        if (card.isSecondaryValueModified)
        {
            if (card.secondaryValue > 0)
            {
                return new ColoredString(card.secondaryValue, COOLDOWN_INCOMPLETE_COLOR, card.transparency);
            }
            else
            {
                return new ColoredString(card.secondaryValue, Settings.GREEN_TEXT_COLOR, card.transparency);
            }
        }
        else
        {
            return new ColoredString(card.secondaryValue, Settings.CREAM_COLOR, card.transparency);
        }
    }

    public void ProgressCooldownAndTrigger(AbstractMonster m)
    {
        ProgressCooldownAndTrigger(1, m);
    }

    public void ProgressCooldownAndTrigger(int progress, AbstractMonster m)
    {
        if (ProgressCooldown(progress))
        {
            if (m == null || GameUtilities.IsDeadOrEscaped(m))
            {
                onCooldownCompleted.Invoke(GameUtilities.GetRandomEnemy(true));
            }
            else
            {
                onCooldownCompleted.Invoke(m);
            }
        }
    }

    public boolean ProgressCooldown()
    {
        return ProgressCooldown(1);
    }

    public boolean ProgressCooldown(int amount)
    {
        boolean activate;
        int newValue;
        if (card.secondaryValue <= 0)
        {
            newValue = GetBase();
            activate = true;
        }
        else
        {
            newValue = Math.max(0, card.secondaryValue - amount);
            activate = false;
        }

        for (AbstractCard c : GameUtilities.GetAllInBattleInstances(card.uuid))
        {
            AnimatorCard card = (AnimatorCard) c;
            card.secondaryValue = newValue;
            card.isSecondaryValueModified = (card.secondaryValue != card.baseSecondaryValue);
        }

        return activate;
    }

    public int GetCurrent()
    {
        return card.secondaryValue;
    }

    public int GetBase()
    {
        return card.baseSecondaryValue;
    }
}

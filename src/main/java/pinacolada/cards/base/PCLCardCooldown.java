package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.ColoredString;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class PCLCardCooldown
{
    private final static Color COOLDOWN_INCOMPLETE_COLOR = Settings.GREEN_TEXT_COLOR.cpy().lerp(Settings.CREAM_COLOR, 0.5f);
    private final ActionT1<AbstractMonster> onCooldownCompleted;
    public final PCLCard card;
    public final FuncT0<PCLCard> cardConstructor;
    public final boolean canProgressOnDraw;
    public final boolean canProgressOnManualDiscard;
    public final boolean canProgressFromExhaustPile;
    public final boolean shouldReplaceCard;


    public PCLCardCooldown(PCLCard card, int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted) {
        this(card,baseCooldown,cooldownUpgrade,onCooldownCompleted,false,false,false);
    }

    public PCLCardCooldown(PCLCard card, int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted, boolean canProgressOnManualDiscard, boolean canProgressFromExhaustPile, boolean canProgressOnDraw)
    {
        card.baseCooldownValue = card.cooldownValue = baseCooldown;
        card.upgrade_cooldownValue = cooldownUpgrade;
        this.onCooldownCompleted = onCooldownCompleted;
        this.canProgressOnManualDiscard = canProgressOnManualDiscard;
        this.canProgressFromExhaustPile = canProgressFromExhaustPile;
        this.canProgressOnDraw = canProgressOnDraw;
        this.shouldReplaceCard = false;
        this.cardConstructor = null;
        this.card = card;
    }

    public PCLCardCooldown(PCLCard card, int baseCooldown, int cooldownUpgrade, FuncT0<PCLCard> cardConstructor) {
        this(card,baseCooldown,cooldownUpgrade,cardConstructor,false,false, false, false);
    }

    public PCLCardCooldown(PCLCard card, int baseCooldown, int cooldownUpgrade, FuncT0<PCLCard> cardConstructor, boolean canProgressOnManualDiscard, boolean canProgressFromExhaustPile, boolean canProgressOnDraw, boolean shouldReplaceCard)
    {
        card.baseCooldownValue = card.cooldownValue = baseCooldown;
        card.upgrade_cooldownValue = cooldownUpgrade;
        this.onCooldownCompleted = null;
        this.canProgressOnManualDiscard = canProgressOnManualDiscard;
        this.canProgressFromExhaustPile = canProgressFromExhaustPile;
        this.canProgressOnDraw = canProgressOnDraw;
        this.shouldReplaceCard = shouldReplaceCard;
        this.cardConstructor = cardConstructor;
        this.card = card;
    }

    public ColoredString GetCooldownValueString()
    {
        if (card.cooldownValue < card.baseCooldownValue)
        {
            if (card.cooldownValue > 0)
            {
                return new ColoredString(card.cooldownValue, COOLDOWN_INCOMPLETE_COLOR);
            }
            else
            {
                return new ColoredString(card.cooldownValue, Settings.GREEN_TEXT_COLOR);
            }
        }
        else
        {
            return new ColoredString(card.cooldownValue, Settings.CREAM_COLOR);
        }
    }

    public boolean ProgressCooldownAndTrigger(AbstractMonster m)
    {
        return ProgressCooldownAndTrigger(1, m);
    }

    public boolean ProgressCooldownAndTrigger(int progress, AbstractMonster m)
    {
        boolean canProgress = PCLCombatStats.OnCooldownTriggered(card, this);
        if (canProgress && ProgressCooldown(progress))
        {
            if (onCooldownCompleted != null) {
                if (m == null || PCLGameUtilities.IsDeadOrEscaped(m))
                {
                    onCooldownCompleted.Invoke(PCLGameUtilities.GetRandomEnemy(true));
                }
                else
                {
                    onCooldownCompleted.Invoke(m);
                }
            }
            else if (cardConstructor != null) {
                if (shouldReplaceCard) {
                    PCLActions.Bottom.ReplaceCard(card.uuid, cardConstructor.Invoke());
                }
                else {
                    PCLActions.Bottom.MakeCardInDiscardPile(cardConstructor.Invoke());
                    PCLActions.Last.ModifyAllInstances(card.uuid).AddCallback(PCLActions.Bottom::Exhaust);
                }
            }
            return true;
        }
        return false;
    }

    public boolean ProgressCooldown()
    {
        return ProgressCooldown(1);
    }

    public boolean ProgressCooldown(int amount)
    {
        boolean activate;
        int newValue;
        if (card.cooldownValue <= 0)
        {
            newValue = GetBase();
            activate = true;
        }
        else
        {
            newValue = Math.max(0, card.cooldownValue - amount);
            activate = false;
        }

        for (AbstractCard c : PCLGameUtilities.GetAllInBattleInstances(card.uuid))
        {
            ((PCLCard) c).cooldownValue = newValue;
            ((PCLCard) c).Refresh(null);
        }

        return activate;
    }

    public void ResetCooldown() {
        for (AbstractCard c : PCLGameUtilities.GetAllInBattleInstances(card.uuid))
        {
            ((PCLCard) c).cooldownValue = GetBase();
            ((PCLCard) c).Refresh(null);
        }
    }

    public int GetCurrent()
    {
        return card.cooldownValue;
    }

    public int GetBase()
    {
        return card.baseCooldownValue;
    }
}

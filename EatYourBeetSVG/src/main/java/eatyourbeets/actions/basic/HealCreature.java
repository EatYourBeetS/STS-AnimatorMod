package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TupleT2;

public class HealCreature extends EYBActionWithCallback<TupleT2<AbstractCreature, Integer>>
{
    private static boolean shownTip;

    protected int maxHPRecover;
    protected boolean showEffect = true;
    protected boolean overheal = false;
    protected boolean recover = false;

    public HealCreature(AbstractCreature source, AbstractCreature target, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FAST);

        this.maxHPRecover = CombatStats.MaxHPSinceLastTurn;
        this.canCancel = false;

        Initialize(target, source, amount);

        if (amount <= 0)
        {
            Complete(new TupleT2<>(target, 0));
        }
    }

    public HealCreature Overheal(boolean overheal)
    {
        this.overheal = overheal;

        return this;
    }

    public HealCreature Recover(boolean recover)
    {
        this.recover = recover;

        return this;
    }

    public HealCreature ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    public HealCreature SetCard(AbstractCard card)
    {
        this.card = card;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (target.isPlayer)
        {
            if (recover)
            {
                maxHPRecover = Math.max(maxHPRecover, CombatStats.MaxHPSinceLastTurn);
                amount = Math.min(amount, maxHPRecover - player.currentHealth);

                if (amount <= 0)
                {
                    Complete(new TupleT2<>(target, 0));
                    return;
                }
            }

            if (card != null)
            {
                if (card.heal > 0 && !Settings.isEndless)
                {
                    final int tempHP = Math.min(target.currentHealth - amount, amount);
                    if (tempHP > 0)
                    {
                        GameActions.Top.GainTemporaryHP(tempHP);

                        if (!shownTip)
                        {
                            GameActions.Top.WaitRealtime(1f);
                            GameUtilities.SetFtueTip(card.name, GR.Animator.Strings.Misc.HealingWarning, FtueTip.TipType.NO_FTUE);
                            shownTip = true;
                        }
                    }
                }
                else
                {
                    card.heal = amount;
                }
            }
        }

        final int previousHP = target.currentHealth;

        target.heal(amount, false);

        if (recover && target.isPlayer && target.currentHealth > maxHPRecover)
        {
            target.currentHealth = maxHPRecover;
        }

        final int excessDifference = amount - (target.currentHealth - previousHP);
        if (overheal && excessDifference > 0)
        {
            GameActions.Top.GainTemporaryHP(excessDifference);
        }

        if (showEffect)
        {
            if (target.isPlayer)
            {
                AbstractDungeon.topPanel.panelHealEffect();
            }

            GameEffects.Queue.Add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, amount));
        }

        Complete(new TupleT2<>(target, target.currentHealth - previousHP));
    }
}
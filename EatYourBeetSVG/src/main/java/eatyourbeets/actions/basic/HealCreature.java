package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class HealCreature extends EYBAction
{
    private static boolean shownTip;

    protected boolean showEffect = true;

    public HealCreature(AbstractCreature source, AbstractCreature target, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FAST);

        this.canCancel = false;

        Initialize(target, source, amount);
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
        if (amount <= 0)
        {
            Complete();
            return;
        }

        if (target.isPlayer && card != null)
        {
            if (card.heal > 0)
            {
                final int tempHP = Math.min(target.currentHealth - amount, amount);
                if (tempHP > 0)
                {
                    GameActions.Top.GainTemporaryHP(tempHP);

                    if (!shownTip)
                    {
                        GameActions.Top.WaitRealtime(1f);
                        AbstractDungeon.ftue = new FtueTip(name, GR.Animator.Strings.Misc.HealingWarning,
                        Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, FtueTip.TipType.NO_FTUE);
                        shownTip = true;
                    }
                }
            }
            else
            {
                target.heal(amount, false);
                card.heal = amount;
            }
        }
        else
        {
            target.heal(amount, false);
        }

        if (showEffect)
        {
            if (target.isPlayer)
            {
                AbstractDungeon.topPanel.panelHealEffect();
            }

            GameEffects.Queue.Add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, amount));
        }
    }
}
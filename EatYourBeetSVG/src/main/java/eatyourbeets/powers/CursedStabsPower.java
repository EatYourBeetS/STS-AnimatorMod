package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CursedStabsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(CursedStabsPower.class.getSimpleName());

    public CursedStabsPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        updateDescription();
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Pain(), 1, true, true));
        }
    }
}
package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EarthenThornsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EarthenThornsPower.class.getSimpleName());

    private DamageInfo thornsInfo;

    public EarthenThornsPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;
        this.thornsInfo = new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS);

        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.thornsInfo = new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS);
        this.updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner)
        {
            this.flash();
            AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner, this.thornsInfo, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        return damageAmount;
    }

    public void atStartOfTurn()
    {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}

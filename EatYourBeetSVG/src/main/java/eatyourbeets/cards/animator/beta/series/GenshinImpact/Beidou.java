package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.CounterAttackPower;
import eatyourbeets.utilities.GameActions;

public class Beidou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Beidou.class).SetAttack(3, CardRarity.COMMON).SetSeriesFromClassPackage(true);

    public Beidou()
    {
        super(DATA);

        Initialize(3, 16, 3, 1);
        SetUpgrade(1, 2, 0, 0);
        SetAffinity_Red(1,1,1);
        SetAffinity_Orange(1,0,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new CounterAttackPower(p, magicNumber));
        GameActions.Bottom.StackPower(new BeidouPower(p, secondaryValue));
    }

    public static class BeidouPower extends AnimatorPower
    {
        public BeidouPower(AbstractCreature owner, int amount)
        {
            super(owner, Beidou.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            AbstractCreature mo = info.owner;
            if (info.type == DamageInfo.DamageType.NORMAL && owner.currentBlock > 0 && mo != null)
            {
                for (AbstractPower po : mo.powers) {
                    if (po instanceof CommonTriggerablePower) {
                        GameActions.Bottom.Add(((CommonTriggerablePower) po).Trigger());
                    }
                    else if (po instanceof PoisonPower) {
                        GameActions.Bottom.Add(new PoisonLoseHpAction(mo, player, po.amount, AttackEffects.POISON));
                    }
                    else if (po instanceof HealthBarRenderPower) {
                        GameActions.Bottom.DealDamage(player, mo, po.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
                    }
                }
                this.flashWithoutSound();
            }

            return super.onAttacked(info, damageAmount);
        }

        public void atStartOfTurn()
        {
            ReducePower(1);
        }
    }
}


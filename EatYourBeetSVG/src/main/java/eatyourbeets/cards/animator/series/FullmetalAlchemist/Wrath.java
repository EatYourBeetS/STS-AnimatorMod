package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Wrath extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Wrath.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Wrath()
    {
        super(DATA);

        Initialize(10, 12, 3,2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Fire(2, 0, 3);
        SetAffinity_Earth(1, 0, 2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.RaiseFireLevel(magicNumber);
        GameActions.Bottom.StackPower(new WrathPower(p, secondaryValue));

    }

    public static class WrathPower extends AnimatorPower
    {
        public WrathPower(AbstractPlayer owner, int amount)
        {
            super(owner, Wrath.DATA);

            this.amount = amount;

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
        {
            super.onAttack(info, damageAmount, target);

            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
            {
                final int[] damage = DamageInfo.createDamageMatrix(CombatStats.Affinities.GetPowerAmount(Affinity.Fire), true);
                GameActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                this.flash();
            }
        }

        @Override
        public boolean canPlayCard(AbstractCard card) {
            return card.type == CardType.ATTACK && card.cost > 0;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}
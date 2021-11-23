package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Wrath extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Wrath.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Wrath()
    {
        super(DATA);

        Initialize(10, 12, 3,2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2, 0, 3);
        SetAffinity_Orange(1, 0, 2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainMight(magicNumber);
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
        public boolean canPlayCard(AbstractCard card) {
            return card.type == CardType.ATTACK && card.cost > 0;
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (card.type == CardType.ATTACK && card.costForTurn >= 1)
            {
                final int[] damage = DamageInfo.createDamageMatrix(MathUtils.ceil(CombatStats.Affinities.GetPowerAmount(Affinity.Red) / 2f), true);
                GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                this.flashWithoutSound();
            }
        }


        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}
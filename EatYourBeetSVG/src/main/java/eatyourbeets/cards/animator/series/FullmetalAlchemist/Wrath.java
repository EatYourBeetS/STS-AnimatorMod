package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import static eatyourbeets.utilities.GameUtilities.ModifyCostForCombat;

public class Wrath extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Wrath.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Wrath()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Fire();
        SetAffinity_Air();
        SetAffinity_Mind();

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.ChangeStance(WrathStance.STANCE_ID);

        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.MakeCardInHand(new Strike()).AddCallback(card -> {
                if (card.cost >= 0)
                {
                    ModifyCostForCombat(card, 0, false);
                }

                if (upgraded)
                {
                    card.upgrade();
                }
            });
        }
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
package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class Wrath extends PCLCard
{
    public static final PCLCardData DATA = Register(Wrath.class)
            .SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Wrath()
    {
        super(DATA);

        Initialize(10, 11, 3,2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 3);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new WrathPower(p, secondaryValue));

    }

    public static class WrathPower extends PCLPower
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
                final int[] damage = DamageInfo.createDamageMatrix(MathUtils.ceil(PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Red) * 2f), true);
                PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
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
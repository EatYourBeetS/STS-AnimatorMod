package pinacolada.cards.pcl.series.OnePunchMan;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class PuriPuriPrisoner extends PCLCard
{
    public static final PCLCardData DATA = Register(pinacolada.cards.pcl.series.OnePunchMan.PuriPuriPrisoner.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public PuriPuriPrisoner()
    {
        super(DATA);

        Initialize(5, 0, 5);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(2);

        SetAffinityRequirement(PCLAffinity.Red, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.StackPower(new PuriPuriPrisonerPower(p, 1));
        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.StackPower(new CounterAttackPower(p, magicNumber));
        }
    }

    public static class PuriPuriPrisonerPower extends PCLPower
    {
        public PuriPuriPrisonerPower(AbstractCreature owner, int amount)
        {
            super(owner, pinacolada.cards.pcl.series.OnePunchMan.PuriPuriPrisoner.DATA);

            Initialize(amount);
        }

        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            if (type == DamageInfo.DamageType.NORMAL && card != null && card.type == CardType.ATTACK && PCLGameUtilities.HasGreenAffinity(card)) {
                damage *= 2;
            }

            return super.atDamageGive(damage, type, card);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card) {
            super.onAfterCardPlayed(card);
            if (card.type == CardType.ATTACK) {
                if (!PCLGameUtilities.HasGreenAffinity(card)) {
                    PCLActions.Bottom.GainBlock(MathUtils.ceil(card.damage * 0.5f));
                }
                PCLActions.Bottom.Exhaust(card);
                this.RemovePower();
            }

        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            RemovePower(PCLActions.Delayed);
        }
    }
}
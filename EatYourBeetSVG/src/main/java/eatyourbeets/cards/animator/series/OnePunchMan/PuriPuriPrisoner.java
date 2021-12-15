package eatyourbeets.cards.animator.series.OnePunchMan;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.CounterAttackPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PuriPuriPrisoner extends AnimatorCard
{
    public static final EYBCardData DATA = Register(eatyourbeets.cards.animator.series.OnePunchMan.PuriPuriPrisoner.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public PuriPuriPrisoner()
    {
        super(DATA);

        Initialize(5, 0, 5);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(2);

        SetAffinityRequirement(Affinity.Red, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.StackPower(new PuriPuriPrisonerPower(p, 1));
        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.StackPower(new CounterAttackPower(p, magicNumber));
        }
    }

    public static class PuriPuriPrisonerPower extends AnimatorPower
    {
        public PuriPuriPrisonerPower(AbstractCreature owner, int amount)
        {
            super(owner, eatyourbeets.cards.animator.series.OnePunchMan.PuriPuriPrisoner.DATA);

            Initialize(amount);
        }

        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            if (type == DamageInfo.DamageType.NORMAL && card != null && card.type == CardType.ATTACK && GameUtilities.HasGreenAffinity(card)) {
                damage *= 2;
            }

            return super.atDamageGive(damage, type, card);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card) {
            super.onAfterCardPlayed(card);
            if (card.type == CardType.ATTACK) {
                if (!GameUtilities.HasGreenAffinity(card)) {
                    GameActions.Bottom.GainBlock(MathUtils.ceil(card.damage * 0.5f));
                }
                GameActions.Bottom.Exhaust(card);
                this.RemovePower();
            }

        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            RemovePower(GameActions.Delayed);
        }
    }
}
package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EulaLawrence extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EulaLawrence.class).SetAttack(3, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    public static final int MODIFIER = 50;

    public EulaLawrence()
    {
        super(DATA);

        Initialize(20, 0, MODIFIER, 2);
        SetUpgrade(5,0,0,0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
        GameActions.Bottom.DealDamage(this,m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new EulaLawrencePower(m, secondaryValue));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this) && c instanceof EYBCard && ((EYBCard) c).attackType == EYBAttackType.Normal)
        {
            GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                GameUtilities.IncreaseDamage(c, magicNumber, false);
            });
            GameActions.Bottom.Flash(this);
        }
    }

    public static class EulaLawrencePower extends AnimatorPower
    {
        public EulaLawrencePower(AbstractCreature owner, int amount)
        {
            super(owner, EulaLawrence.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public float atDamageReceive(float damage, DamageInfo.DamageType damageType, AbstractCard card)
        {
            return enabled && damageType == DamageInfo.DamageType.NORMAL && card instanceof EYBCard && ((EYBCard) card).attackType == EYBAttackType.Normal ? damage * (1 + (amount * EulaLawrence.MODIFIER / 100f)) : damage;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, EulaLawrence.MODIFIER);
        }
    }
}
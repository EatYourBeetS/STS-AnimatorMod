package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class EulaLawrence extends PCLCard
{
    public static final PCLCardData DATA = Register(EulaLawrence.class).SetAttack(3, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    public static final int MODIFIER = 50;

    public EulaLawrence()
    {
        super(DATA);

        Initialize(20, 0, MODIFIER, 2);
        SetUpgrade(5,0,0,0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Light(0, 0, 2);

        SetAffinityRequirement(PCLAffinity.General, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
        PCLActions.Bottom.DealCardDamage(this,m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.StackPower(new EulaLawrencePower(m, secondaryValue));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this) && c instanceof PCLCard && ((PCLCard) c).attackType == PCLAttackType.Normal)
        {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                PCLGameUtilities.IncreaseDamage(c, magicNumber, false);
            });
            PCLActions.Bottom.Flash(this);
        }
    }

    public static class EulaLawrencePower extends PCLPower
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
            return enabled && damageType == DamageInfo.DamageType.NORMAL && card instanceof PCLCard && ((PCLCard) card).attackType == PCLAttackType.Normal ? damage * (1 + (amount * EulaLawrence.MODIFIER / 100f)) : damage;
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
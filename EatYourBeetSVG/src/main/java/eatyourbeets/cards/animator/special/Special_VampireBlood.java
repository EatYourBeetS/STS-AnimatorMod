package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Special_VampireBlood extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Special_VampireBlood.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetMaxCopies(1);
    public static final int RECOVER_AMOUNT = 2;
    public static final int HP_COST = 7;

    public Special_VampireBlood()
    {
        super(DATA);

        Initialize(0, 0, 2, RECOVER_AMOUNT);
        SetCostUpgrade(-1);

        SetAffinity_Dark(2);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(HP_COST);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new OfferingEffect(), 0f);
        GameActions.Bottom.LoseHP(magicNumber, AttackEffects.DARK);
        GameActions.Bottom.GainCorruption(1, true);
        GameActions.Bottom.StackPower(new Special_VampireBloodPower(p));
    }

    public static class Special_VampireBloodPower extends AnimatorClickablePower
    {
        public Special_VampireBloodPower(AbstractCreature owner)
        {
            super(owner, Special_VampireBlood.DATA, PowerTriggerConditionType.LoseMixedHP, HP_COST);

            triggerCondition.SetUses(1, true, true);

            Initialize(1);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, RECOVER_AMOUNT);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (usedCard.type == CardType.ATTACK)
            {
                GameActions.Bottom.RecoverHP(RECOVER_AMOUNT)
                .AddCallback(info ->
                {
                    if (info.V2 > 0)
                    {
                        this.flashWithoutSound();
                    }
                });
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.GainEnergy(1);
        }
    }
}
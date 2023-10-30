package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.replacement.TemporaryEnvenomPower;
import eatyourbeets.utilities.GameActions;

public class AcuraShin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraShin.class)
            .SetPower(2, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeries(CardSeries.HitsugiNoChaika);
    public static final int ENVENOM_AMOUNT = 1;

    public AcuraShin()
    {
        super(DATA);

        Initialize(0,0, 1, ENVENOM_AMOUNT);

        SetAffinity_Green(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AcuraShinPower(p, magicNumber));
    }

    public static class AcuraShinPower extends AnimatorClickablePower
    {
        public AcuraShinPower(AbstractCreature owner, int amount)
        {
            super(owner, AcuraShin.DATA, PowerTriggerConditionType.Discard, 1);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, ENVENOM_AMOUNT, amount);
        }

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card)
        {
            if (card != null && card.type == CardType.ATTACK  && type == DamageInfo.DamageType.NORMAL && (card.costForTurn == 0 || card.freeToPlay()))
            {
                damage += amount;
            }

            return super.atDamageFinalGive(damage, type, card);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.StackPower(new TemporaryEnvenomPower(owner, ENVENOM_AMOUNT));
        }
    }
}
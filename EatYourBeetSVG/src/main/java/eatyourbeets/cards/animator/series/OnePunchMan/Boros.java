package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Boros extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Boros.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 1;

    public Boros()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);

        SetDelayed(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new BorosPower(p, magicNumber));
    }

    public static class BorosPower extends AnimatorClickablePower
    {
        public BorosPower(AbstractCreature owner, int amount)
        {
            super(owner, Boros.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            triggerCondition.SetUses(-1, false, false);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ReducePower(1);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if ((card.type == AbstractCard.CardType.POWER) && GameUtilities.CanPlayTwice(card))
            {
                GameActions.Top.PlayCopy(card, JUtils.SafeCast(action.target, AbstractMonster.class));
                this.flash();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.GainForce(1, true);
            stackPower(1);
        }
    }
}
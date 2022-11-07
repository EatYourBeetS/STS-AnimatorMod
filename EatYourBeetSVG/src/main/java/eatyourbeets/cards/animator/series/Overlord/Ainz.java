package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ainz extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ainz.class)
            .SetPower(7, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final int CHANNEL_AMOUNT = 3;
    public static final int POWER_ENERGY_COST = 3;

    public Ainz()
    {
        super(DATA);

        Initialize(0, 0, POWER_ENERGY_COST, CHANNEL_AMOUNT);
        SetCostUpgrade(-1);

        SetAffinity_Red(1);
        SetAffinity_Blue(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        GameActions.Bottom.Motivate(this, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Motivate(this, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AinzPower(p, 1));
    }

    public static class AinzPower extends AnimatorClickablePower
    {
        public AinzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Ainz.DATA, PowerTriggerConditionType.Energy, Ainz.POWER_ENERGY_COST);

            triggerCondition.SetUses(-1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, Ainz.CHANNEL_AMOUNT, amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE, 0.9f, 1.1f);
            GameActions.Bottom.BorderLongFlash(Color.valueOf("3d0066"));
            GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.9f, 1.1f);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (GameUtilities.IsHighCost(card))
            {
                GameActions.Bottom.GainEnergy(amount);
                flash();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.ChannelOrbs(Chaos::new, Ainz.CHANNEL_AMOUNT);
        }
    }
}
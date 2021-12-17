package pinacolada.cards.pcl.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.SFX;
import pinacolada.orbs.pcl.Chaos;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.stances.DesecrationStance;
import pinacolada.utilities.PCLActions;

public class Ainz extends PCLCard
{
    public static final PCLCardData DATA = Register(Ainz.class)
            .SetPower(7, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final int CHANNEL_AMOUNT = 3;
    public static final int POWER_ENERGY_COST = 10;
    public static final PCLAffinity[] AFFINITIES = new PCLAffinity[] {PCLAffinity.Red, PCLAffinity.Blue, PCLAffinity.Orange, PCLAffinity.Dark};

    public Ainz()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(2);
        SetCostUpgrade(-1);

        SetProtagonist(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(POWER_ENERGY_COST, CHANNEL_AMOUNT);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.Motivate(this, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (block > 0)
        {
            PCLActions.Bottom.GainBlock(block);
        }

        PCLActions.Bottom.StackPower(new AinzPower(p, magicNumber));
    }

    public static class AinzPower extends PCLClickablePower
    {
        public AinzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Ainz.DATA, PowerTriggerConditionType.Affinity, Ainz.POWER_ENERGY_COST, null, null, PCLAffinity.Dark);

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

            PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE, 0.9f, 1.1f);
            PCLActions.Bottom.BorderLongFlash(Color.valueOf("3d0066"));
            PCLActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.9f, 1.1f);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            for (PCLAffinity a : AFFINITIES)
            {
                PCLActions.Bottom.StackAffinityPower(a, amount, DesecrationStance.IsActive());
            }

            this.flash();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.ChannelOrbs(Chaos::new, Ainz.CHANNEL_AMOUNT);
        }
    }
}
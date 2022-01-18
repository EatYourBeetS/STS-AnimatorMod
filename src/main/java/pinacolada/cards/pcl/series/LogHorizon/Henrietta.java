package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Henrietta extends PCLCard
{
    public static final PCLCardData DATA = Register(Henrietta.class)
            .SetPower(2, CardRarity.RARE)
            .SetMultiformData(2)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    private static final int POWER_COST = 9;

    public Henrietta()
    {
        super(DATA);

        Initialize(0, 0, 3, POWER_COST);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new HenriettaPower(p, magicNumber));
    }

    public static class HenriettaPower extends PCLClickablePower
    {
        public HenriettaPower(AbstractPlayer owner, int amount)
        {
            super(owner, Henrietta.DATA, PowerTriggerConditionType.Affinity, Henrietta.POWER_COST);
            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Bottom.TryChooseAffinity(name).AddConditionalCallback(choices -> {
               if (choices.size() > 0) {
                   PCLActions.Top.StackAffinityPower(choices.get(0).Affinity, amount);
               }
            });
        }

        @Override
        public void onInitialApplication() {
            super.onInitialApplication();
            PCLGameUtilities.SetCanChooseAffinityReroll(true);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            PCLGameUtilities.SetCanChooseAffinityReroll(false);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, POWER_COST, amount);
        }
    }
}
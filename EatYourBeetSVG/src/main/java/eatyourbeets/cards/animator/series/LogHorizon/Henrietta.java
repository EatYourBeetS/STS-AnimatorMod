package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Henrietta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Henrietta.class)
            .SetPower(2, CardRarity.RARE)
            .SetMultiformData(2)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    private static final int POWER_COST = 10;

    public Henrietta()
    {
        super(DATA);

        Initialize(0, 0, 1, POWER_COST);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
        SetDelayed(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HenriettaPower(p, magicNumber));
    }

    public static class HenriettaPower extends AnimatorClickablePower
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

            GameActions.Bottom.TryChooseAffinity(name).AddConditionalCallback(choices -> {
               if (choices.size() > 0) {
                   GameUtilities.AddAffinityPowerLevel(choices.get(0).Affinity, amount);
               }
            });
        }

        @Override
        public void onInitialApplication() {
            super.onInitialApplication();
            GameUtilities.SetCanChooseAffinityReroll(true);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            GameUtilities.SetCanChooseAffinityReroll(false);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, POWER_COST, amount);
        }
    }
}
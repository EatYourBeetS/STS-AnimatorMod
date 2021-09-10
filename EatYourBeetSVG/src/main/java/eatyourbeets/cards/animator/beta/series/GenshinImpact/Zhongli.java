package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage().SetMultiformData(2);
    private static final int POWER_ENERGY_COST = 1;

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(2, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetInnate(true);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form != 1);
        }
        return super.SetForm(form, timesUpgraded);
    };


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new ZhongliPower(p, this.magicNumber));
    }

    public static class ZhongliPower extends AnimatorClickablePower
    {
        public ZhongliPower(AbstractPlayer owner, int amount)
        {
            super(owner, Zhongli.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            this.amount = amount;
            this.triggerCondition.SetOneUsePerPower(true);

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.ChannelOrb(new Earth());
            GameActions.Bottom.GainPlatedArmor(amount);
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            Earth earthOrb = JUtils.SafeCast(orb, Earth.class);

            if (earthOrb != null) {
                GameActions.Bottom.GainBlock(earthOrb.projectilesCount / 2);
            }
        }
    }
}
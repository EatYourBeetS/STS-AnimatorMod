package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Biyorigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Biyorigo.class)
            .SetPower(2, CardRarity.RARE)
            .SetMultiformData(2)
            .SetSeriesFromClassPackage();
    public static final int COST = 7;

    public Biyorigo()
    {
        super(DATA);

        Initialize(0, 0, 2, COST);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetDelayed(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(COST);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainMalleable(magicNumber);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, magicNumber));
    }

    public static class BiyorigoPower extends AnimatorClickablePower
    {

        public BiyorigoPower(AbstractCreature owner, int amount)
        {
            super(owner, Biyorigo.DATA, PowerTriggerConditionType.Affinity, COST, null, null, Affinity.Green);

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, COST, amount);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (info.type == DamageInfo.DamageType.NORMAL && damageAmount < info.output)
            {
                GameActions.Bottom.ApplyPoison(TargetHelper.Normal(info.owner), amount);
            }

            return super.onAttacked(info, damageAmount);
        }
        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            GameActions.Bottom.GainThorns(2);
        }
    }
}
package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class Biyorigo extends PCLCard
{
    public static final PCLCardData DATA = Register(Biyorigo.class)
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
        PCLActions.Bottom.GainMalleable(magicNumber);
        PCLActions.Bottom.StackPower(new BiyorigoPower(p, magicNumber));
    }

    public static class BiyorigoPower extends PCLClickablePower
    {

        public BiyorigoPower(AbstractCreature owner, int amount)
        {
            super(owner, Biyorigo.DATA, PowerTriggerConditionType.Affinity, COST, null, null, PCLAffinity.Green);

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
            if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner.isPlayer != owner.isPlayer)
            {
                PCLActions.Bottom.ApplyPoison(TargetHelper.Normal(info.owner), amount);
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Bottom.GainThorns(2);
        }
    }
}
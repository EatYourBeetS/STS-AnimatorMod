package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Darkness extends PCLCard
{
    public static final PCLCardData DATA = Register(Darkness.class)
            .SetSkill(2, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Self)
            .SetSeriesFromClassPackage();

    public Darkness()
    {
        super(DATA);

        Initialize(0, 14, 2, 5);
        SetUpgrade(0, 1, 0, -1);

        SetAffinity_Red(1);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new DarknessPower(p, magicNumber));

        if (info.IsSynergizing && this.costForTurn > 0) {
            PCLGameUtilities.ModifyCostForCombat(this,-1,true);
            this.baseBlock -= secondaryValue;
        }
    }

    public static class DarknessPower extends PCLPower
    {
        public DarknessPower(AbstractPlayer owner, int amount)
        {
            super(owner, Darkness.DATA);

            Initialize(amount);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
            {
                PCLActions.Bottom.GainPlatedArmor(amount);
                RemovePower();
                this.flash();
            }
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            RemovePower();
        }

    }

}
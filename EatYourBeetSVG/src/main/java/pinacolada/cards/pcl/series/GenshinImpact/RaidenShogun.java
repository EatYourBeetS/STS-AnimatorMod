package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.powers.special.Amplification_DarkPower;
import pinacolada.powers.special.Amplification_LightningPower;
import pinacolada.utilities.PCLActions;

public class RaidenShogun extends PCLCard
{
    public static final PCLCardData DATA = Register(RaidenShogun.class).SetPower(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage(true)
            .SetMultiformData(2);

    public RaidenShogun()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(auxiliaryData.form != 0);
        SetInnate(auxiliaryData.form == 0);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
            SetInnate(form != 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyPower(new RaidenShogunPower(p, this.magicNumber));
        PCLActions.Bottom.StackPower(new Amplification_DarkPower(p, secondaryValue, PCLAffinity.Dark));
        PCLActions.Bottom.StackPower(new Amplification_LightningPower(p, secondaryValue, PCLAffinity.Dark));
    }

    public static class RaidenShogunPower extends PCLPower
    {

        public RaidenShogunPower(AbstractCreature owner, int amount)
        {
            super(owner, RaidenShogun.DATA);

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            PCLActions.Bottom.ChannelOrb(new Lightning());
            PCLActions.Bottom.ApplyElectrified(TargetHelper.AllCharacters(), amount);
        }


        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Dark.ORB_ID.equals(orb.ID) || Lightning.ORB_ID.equals(orb.ID)) {
                PCLActions.Bottom.StackAffinityPower(PCLAffinity.Dark, amount);
                flash();
            }
        }
    }
}
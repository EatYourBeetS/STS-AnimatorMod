package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.Amplification_DarkPower;
import eatyourbeets.powers.animator.Amplification_LightningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class RaidenShogun extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RaidenShogun.class).SetPower(3, CardRarity.RARE)
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
        GameActions.Bottom.ApplyPower(new RaidenShogunPower(p, this.magicNumber));
        GameActions.Bottom.StackPower(new Amplification_DarkPower(p, secondaryValue, Affinity.Dark));
        GameActions.Bottom.StackPower(new Amplification_LightningPower(p, secondaryValue, Affinity.Dark));
    }

    public static class RaidenShogunPower extends AnimatorPower
    {

        public RaidenShogunPower(AbstractCreature owner, int amount)
        {
            super(owner, RaidenShogun.DATA);

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            GameActions.Bottom.ChannelOrb(new Lightning());
            GameActions.Bottom.ApplyElectrified(TargetHelper.AllCharacters(), amount);
        }


        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Dark.ORB_ID.equals(orb.ID) || Lightning.ORB_ID.equals(orb.ID)) {
                GameActions.Bottom.StackAffinityPower(Affinity.Dark, amount);
                flash();
            }
        }
    }
}
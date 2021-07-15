package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage();

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 3, 1, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(2, 0, 0);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new ZhongliPower(p, this.magicNumber, this.secondaryValue));
    }

    public static class ZhongliPower extends AnimatorPower
    {
        private final int secondaryAmount;

        public ZhongliPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, Zhongli.DATA);

            this.amount = amount;
            this.secondaryAmount = secondaryAmount;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            int energy = Math.min(secondaryAmount, EnergyPanel.getCurrentEnergy());
            if (energy > 0)
            {
                EnergyPanel.useEnergy(energy);
                GameActions.Bottom.ChannelOrb(new Earth());
                GameActions.Bottom.GainPlatedArmor(amount);
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            if (Earth.ORB_ID.equals(orb.ID)) {
                GameActions.Bottom.GainBlock(orb.evokeAmount / 2);
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, secondaryAmount, amount);
        }
    }
}
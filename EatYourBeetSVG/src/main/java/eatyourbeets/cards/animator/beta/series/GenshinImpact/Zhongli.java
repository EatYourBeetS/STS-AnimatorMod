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

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage();

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Orange(2, 0, 0);
    }

    public void OnUpgrade() {
        SetInnate(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new ZhongliPower(p, this.magicNumber));
    }

    public static class ZhongliPower extends AnimatorClickablePower
    {
        public ZhongliPower(AbstractPlayer owner, int amount)
        {
            super(owner, Zhongli.DATA, PowerTriggerConditionType.Energy, 2);

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

            if (Earth.ORB_ID.equals(orb.ID)) {
                GameActions.Bottom.GainBlock(orb.evokeAmount / 2);
            }
        }
    }
}
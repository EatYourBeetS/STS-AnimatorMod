package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Blue(1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new KagariPower(p, secondaryValue, magicNumber));
    }

    public static class KagariPower extends AnimatorPower implements OnChannelOrbSubscriber
    {
        private final int secondaryAmount;
        public KagariPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, Kagari.DATA);

            this.amount = amount;
            this.secondaryAmount = secondaryAmount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, secondaryAmount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ResetAmount();
        }

        @Override
        public void OnChannelOrb(AbstractOrb orb) {
            if (Earth.ORB_ID.equals(orb.ID) && owner.isPlayer && amount > 0) {
                GameActions.Bottom.GainWillpower(amount, player.stance.ID.equals(WillpowerStance.STANCE_ID));
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, secondaryAmount);
                this.amount -= 1;
                updateDescription();
                flash();
            }
        }
    }
}
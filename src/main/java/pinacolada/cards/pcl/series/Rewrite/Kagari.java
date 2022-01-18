package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Earth;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.stances.EnduranceStance;
import pinacolada.utilities.PCLActions;

public class Kagari extends PCLCard
{
    public static final PCLCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainResistance(magicNumber);
        PCLActions.Bottom.StackPower(new KagariPower(p, 1, secondaryValue));
    }

    public static class KagariPower extends PCLPower implements OnChannelOrbSubscriber
    {
        private final int secondaryAmount;
        public KagariPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, Kagari.DATA);

            this.secondaryAmount = secondaryAmount;

            Initialize(amount);
            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, GetEndurance(), secondaryAmount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ResetAmount();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onChannelOrb.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onChannelOrb.Unsubscribe(this);
        }

        @Override
        public void OnChannelOrb(AbstractOrb orb) {
            if (Earth.ORB_ID.equals(orb.ID) && amount > 0) {
                PCLActions.Bottom.GainEndurance(GetEndurance(), player.stance.ID.equals(EnduranceStance.STANCE_ID));
                PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.Shackles, secondaryAmount);
                this.amount -= 1;
                updateDescription();
                flash();
            }
        }

        protected int GetEndurance() {
            return player.stance.ID.equals(EnduranceStance.STANCE_ID) ? 1 + secondaryAmount : secondaryAmount;
        }
    }
}
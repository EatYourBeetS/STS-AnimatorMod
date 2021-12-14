package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class AkaneSenri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AkaneSenri.class).SetPower(3, CardRarity.RARE).SetSeriesFromClassPackage();

    public AkaneSenri()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 0);
        SetEthereal(true);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AkaneSenriPower(p, magicNumber, secondaryValue));
        GameActions.Bottom.ModifyTag(player.drawPile, magicNumber, HASTE, true);
    }

    public static class AkaneSenriPower extends AnimatorClickablePower implements OnShuffleSubscriber
    {
        private static final int BLESSING_COST = 7;
        protected int secondaryAmount;

        public AkaneSenriPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, AkaneSenri.DATA, PowerTriggerConditionType.Affinity, BLESSING_COST, null, null, Affinity.Light);
            this.secondaryAmount = secondaryAmount;
            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            CombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            CombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            AbstractOrb darkOrb = new Dark();
            GameActions.Bottom.ChannelOrb(darkOrb);
            GameActions.Bottom.TriggerOrbPassive(darkOrb,player.hand.size());
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, secondaryAmount, BLESSING_COST);
        }

        @Override
        public void OnShuffle(boolean triggerRelics) {
            if (!owner.powers.contains(this))
            {
                CombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            GameActions.Bottom.ModifyTag(player.drawPile, amount, HASTE, true);
            GameActions.Bottom.GainVelocity(secondaryAmount);
        }
    }
}
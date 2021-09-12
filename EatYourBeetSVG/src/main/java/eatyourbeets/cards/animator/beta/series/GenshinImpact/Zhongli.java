package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage().SetMultiformData(2);
    private static final int POWER_ENERGY_COST = 2;

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(2, 0, 0);
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetDelayed(false);
            SetInnate(true);
        }
        else {
            SetDelayed(true);
        }
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new ZhongliPower(p, this.magicNumber, this.secondaryValue));
    }

    public static class ZhongliPower extends AnimatorClickablePower implements OnStartOfTurnPostDrawSubscriber
    {
        public int gainAmount = 0;
        public int secondaryValue;

        public ZhongliPower(AbstractPlayer owner, int amount, int secondaryValue)
        {
            super(owner, Zhongli.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            this.amount = amount;
            this.secondaryValue = secondaryValue;
            this.triggerCondition.SetOneUsePerPower(true);

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.ChannelOrb(new Earth());
            gainAmount += amount;
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, secondaryValue);
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            Earth earthOrb = JUtils.SafeCast(orb, Earth.class);

            if (earthOrb != null) {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        }

        @Override
        public void OnStartOfTurnPostDraw() {
            GameActions.Bottom.GainBalance(gainAmount, true);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            gainAmount = 0;
        }
    }
}
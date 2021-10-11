package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 2;
    private static final int TRIGGER_LIMIT = 3;

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ZhongliPower(p, this.magicNumber, this.secondaryValue));
    }

    public static class ZhongliPower extends AnimatorClickablePower
    {
        private static final CardEffectChoice choices = new CardEffectChoice();

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
            super.OnUse(m);
            GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(TRIGGER_LIMIT, false, true, null).SetFilter(orb -> Earth.ORB_ID.equals(orb.ID)));
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
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(secondaryValue, Color.WHITE, c.a);
        }
    }
}
package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Tatsumaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tatsumaki.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrb(new Aether());
        GameActions.Bottom.StackPower(new TatsumakiPower(p, magicNumber));
    }

    public static class TatsumakiPower extends AnimatorClickablePower
    {
        public TatsumakiPower(AbstractCreature owner, int amount)
        {
            super(owner, Tatsumaki.DATA, PowerTriggerConditionType.Energy, 1);

            this.triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower(GameActions.Last);
        }

        @Override
        protected void onAmountChanged(int previousAmount, int difference)
        {
            GameActions.Bottom.GainFocus(difference).IgnoreArtifact(true).ShowEffect(difference > 0, true);

            super.onAmountChanged(previousAmount, difference);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        }
    }
}
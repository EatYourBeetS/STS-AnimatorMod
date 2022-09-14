package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Arpeggio.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 2;

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, POWER_ENERGY_COST, 3);

        SetAffinity_Blue(2);

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
        GameActions.Bottom.StackPower(new ArpeggioPower(p, secondaryValue));
    }

    public static class ArpeggioPower extends AnimatorClickablePower implements OnAffinitySealedSubscriber
    {
        public ArpeggioPower(AbstractCreature owner, int amount)
        {
            super(owner, Arpeggio.DATA, PowerTriggerConditionType.Energy, Arpeggio.POWER_ENERGY_COST);

            triggerCondition.SetUses(1, false, true);
            canBeZero = true;

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void OnAffinitySealed(EYBCard card, boolean manual)
        {
            if (amount > 0 && card instanceof AffinityToken)
            {
                GameActions.Bottom.GainEnergy(1);
                reducePower(1);
                flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.GainOrbSlots(1);
            GameActions.Bottom.ChannelOrbs(Earth::new, 1);
        }
    }
}
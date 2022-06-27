package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Envy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Envy.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int TEMP_HP_ENERGY_COST = 2;

    public Envy()
    {
        super(DATA);

        Initialize(0, 0, TEMP_HP_ENERGY_COST);

        SetAffinity_Star(1, 1, 0);

        SetDelayed(true);
        SetEthereal(true);
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
        GameActions.Bottom.StackPower(new EnvyPower(p, 1));
    }

    public static class EnvyPower extends AnimatorClickablePower implements OnAffinitySealedSubscriber
    {
        private int vitality;

        public EnvyPower(AbstractPlayer owner, int amount)
        {
            super(owner, Envy.DATA, PowerTriggerConditionType.Energy, Envy.TEMP_HP_ENERGY_COST);

            triggerCondition.SetUses(1, true, false);
            canBeZero = true;

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAffinitySealed.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAffinitySealed.Unsubscribe(this);
        }

        @Override
        public void OnAffinitySealed(EYBCard card, boolean manual)
        {
            if (amount > 0)
            {
                GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(card));
                reducePower(1);
                flashWithoutSound();
            }
        }

        @Override
        public void update(int slot)
        {
            super.update(slot);

            if (GR.UI.Elapsed25())
            {
                UpdateVitality();
            }
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, vitality, amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            UpdateVitality();
            GameActions.Bottom.GainVitality(vitality);
        }

        private void UpdateVitality()
        {
            final int newValue = GameUtilities.GetHealthPercentage(player) < 0.25f ? 2 : 1;
            if (newValue != vitality)
            {
                vitality = newValue;
                updateDescription();
            }
        }
    }
}
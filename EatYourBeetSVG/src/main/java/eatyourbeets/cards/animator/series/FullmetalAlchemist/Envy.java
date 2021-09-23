package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorClickablePower;
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

        Initialize(0, 0);

        SetAffinity_Star(1, 1, 0);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EnvyPower(p, 1));
    }

    public static class EnvyPower extends AnimatorClickablePower
    {
        private int vitality;

        public EnvyPower(AbstractPlayer owner, int amount)
        {
            super(owner, Envy.DATA, PowerTriggerConditionType.Energy, Envy.TEMP_HP_ENERGY_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
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
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Last.Callback(() ->
            {
                GameActions.Top.ModifyAffinityLevel(player.hand, amount, Affinity.General, 2, false)
                .SetFilter(EnvyPower::HasUpgradableAffinities);
                flash();
            });
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
            final int newValue = GameUtilities.GetHealthPercentage(player) < 0.2f ? 2 : 1;
            if (newValue != vitality)
            {
                vitality = newValue;
                updateDescription();
            }
        }

        private static boolean HasUpgradableAffinities(AbstractCard c)
        {
            final EYBCardAffinities a = GameUtilities.GetAffinities(c);
            if (a != null)
            {
                if (a.Star != null && a.Star.level == 1)
                {
                    return true;
                }

                for (EYBCardAffinity affinity : a.List)
                {
                    if (affinity.level == 1)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
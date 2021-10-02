package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Envy extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(Envy.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int DAMAGE_COST = 5;

    public Envy()
    {
        super(DATA);

        Initialize(0, 0, DAMAGE_COST);

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

    public static class EnvyPower extends AnimatorClickablePower implements OnSynergyCheckSubscriber
    {
        private int vitality;

        public EnvyPower(AbstractPlayer owner, int amount)
        {
            super(owner, Envy.DATA, PowerTriggerConditionType.TakeDelayedDamage, Envy.DAMAGE_COST);

            triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, Envy.DAMAGE_COST, 1);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onSynergyCheck.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergyCheck.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Last.Callback(() ->
            {
                GameActions.Top.ModifyAffinityLevel(player.hand, amount, Affinity.General, 2, false)
                        .SetFilter(EnvyPower::HasUpgradableAffinities);
                flash();
            });
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

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);
            this.amount = Math.max(0, this.amount - 1);
        }

        @Override
        public boolean OnSynergyCheck(AbstractCard a, AbstractCard b)
        {
            return amount > 0;
        }
    }
}
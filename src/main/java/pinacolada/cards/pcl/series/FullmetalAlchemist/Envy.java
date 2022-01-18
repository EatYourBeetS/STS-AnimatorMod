package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Envy extends PCLCard
{
    public static final PCLCardData DATA = Register(Envy.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int DAMAGE_COST = 5;

    public Envy()
    {
        super(DATA);

        Initialize(0, 0, 2, DAMAGE_COST);

        SetAffinity_Star(1, 0, 0);

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
        PCLActions.Bottom.StackPower(new EnvyPower(p, magicNumber));
    }

    public static class EnvyPower extends PCLClickablePower
    {

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
        protected void onAmountChanged(int previousAmount, int difference)
        {
            super.onAmountChanged(previousAmount, difference);
            PCLGameUtilities.AddBaseAffinityRerolls(difference);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Last.Callback(() ->
            {
                PCLActions.Bottom.SelectFromPile(name, 9999, player.hand).SetOptions(true, false).AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.General, 1);
                    }
                });
                flash();
            });
        }
    }
}
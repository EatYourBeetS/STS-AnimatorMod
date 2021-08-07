package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Envy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Envy.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Envy()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1, 1, 0);

        SetEthereal(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (magicNumber > 0) ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyMagicNumber(this, Math.floorDiv(player.maxHealth - player.currentHealth, 5), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new EnvyPower(p, 1));

        int tempHP = Math.floorDiv(p.maxHealth - p.currentHealth, 5);
        if (tempHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(tempHP);
        }
    }

    public static class EnvyPower extends AnimatorPower implements OnSynergyCheckSubscriber
    {
        public EnvyPower(AbstractPlayer owner, int amount)
        {
            super(owner, Envy.DATA);

            Initialize(amount);
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
        public void updateDescription()
        {
            super.updateDescription();

            SetEnabled(amount > 0);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            this.amount = this.baseAmount;
            updateDescription();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            this.amount = Math.max(0, this.amount - 1);
            updateDescription();
        }

        @Override
        public boolean OnSynergyCheck(AbstractCard a, AbstractCard b)
        {
            return amount > 0;
        }
    }
}
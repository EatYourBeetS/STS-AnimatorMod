package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;

public class EnvyPower extends AnimatorPower implements OnSynergyCheckSubscriber
{
    public static final String POWER_ID = CreateFullID(EnvyPower.class);

    private int baseAmount;

    public EnvyPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.baseAmount = this.amount = amount;
        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        this.baseAmount += stackAmount;
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
        super.onInitialApplication();
        this.amount = this.baseAmount;
        updateDescription();
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
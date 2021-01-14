package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

public class EnvyPower extends AnimatorPower implements OnSynergyCheckSubscriber
{
    public static final String POWER_ID = CreateFullID(EnvyPower.class);

    public EnvyPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
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
    public boolean OnSynergyCheck(AbstractCard a, AbstractCard b)
    {
        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        AbstractCard lastCard = GameUtilities.GetLastCardPlayed(true);
        return (lastCard == a) ? cardsPlayed <= amount : cardsPlayed < amount;
    }
}
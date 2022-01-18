package pinacolada.actions.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.utilities.PCLGameUtilities;

public class UsePotionAction extends EYBActionWithCallback<AbstractPotion>
{
    protected AbstractPotion potion;
    protected AbstractCreature target;
    protected boolean shouldRemove = true;

    public UsePotionAction(AbstractPotion potion, AbstractCreature target)
    {
        this(potion, target, 1);
    }

    public UsePotionAction(AbstractPotion potion, AbstractCreature target, int amount)
    {
        super(ActionType.SPECIAL);
        this.target = target;
        this.potion = potion;
        Initialize(amount);
    }

    public UsePotionAction SetShouldRemove(boolean shouldRemove)
    {
        this.shouldRemove = shouldRemove;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (potion.canUse() && target != null && !PCLGameUtilities.IsDeadOrEscaped(target)) {
            for (int i = 0; i < amount; i++) {
                potion.use(target);
            }
        }

        if (shouldRemove) {
            int index = player.potions != null ? player.potions.indexOf(potion) : -1;
            if (index >= 0) {
                player.potions.set(index, new PotionSlot(index));
                player.adjustPotionPositions();
            }
        }

        Complete();
    }
}

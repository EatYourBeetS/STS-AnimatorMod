package eatyourbeets.powers.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.Utilities;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;

public class DarknessPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarknessPower.class.getSimpleName());

    private final AbstractPlayer player;

    public DarknessPower(AbstractPlayer owner, int cards)
    {
        super(owner, POWER_ID);
        this.player = Utilities.SafeCast(this.owner, AbstractPlayer.class);
        this.amount = cards;

        updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        ArrayList<AbstractCard> toUpgrade = new ArrayList<>();

        MutableInt count = new MutableInt(this.amount);
        GetUpgradableCards(toUpgrade, player.hand, count);
        GetUpgradableCards(toUpgrade, player.drawPile, count);
        GetUpgradableCards(toUpgrade, player.discardPile, count);

        for (AbstractCard c : toUpgrade)
        {
            c.upgrade();
            c.superFlash();
        }

        this.flash();

        return super.onLoseHp(damageAmount);
    }

    private void GetUpgradableCards(ArrayList<AbstractCard> toUpgrade, CardGroup group, MutableInt count)
    {
        if (count.intValue() <= 0)
        {
            return;
        }

        int result = 0;
        if (group.size() > 0)
        {
            ArrayList<AbstractCard> upgradable = group.getUpgradableCards().group;
            while (upgradable.size() > 0 && count.intValue() > 0)
            {
                int index = MathUtils.random(upgradable.size() - 1);

                toUpgrade.add(upgradable.get(index));
                upgradable.remove(index);

                count.decrement();
                result += 1;
            }
        }
    }
}

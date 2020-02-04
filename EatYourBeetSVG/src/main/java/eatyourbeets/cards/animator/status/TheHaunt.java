package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.animator.Haunted;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class TheHaunt extends AnimatorCard_Status implements Hidden
{
    public static final String ID = Register_Old(TheHaunt.class);

    public TheHaunt()
    {
        super(ID, 1, CardRarity.RARE, CardTarget.NONE);

        int goldBonus = 0;
        Haunted blight = GetBlight();
        if (blight != null)
        {
            int counter = blight.counter;
            if (counter >= 50)
            {
                goldBonus = 50 + (50 - counter);
            }
            else
            {
                goldBonus = counter;
            }

            goldBonus = Math.max(0, Math.min(999, 15 + goldBonus * 3));
        }

        Initialize(0, 0, goldBonus);

        this.isEthereal = true;
        this.exhaust = true;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        // Do not auto play
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainGold(magicNumber);

        Haunted blight = GetBlight();
        if (blight != null)
        {
            blight.setCounter(blight.counter + 1);
        }
    }

    private static Haunted GetBlight()
    {
        for (AbstractBlight blight : AbstractDungeon.player.blights)
        {
            if (blight instanceof Haunted)
            {
                return (Haunted) blight;
            }
        }

        return null;
    }
}
package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.animator.Haunted;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class TheHaunt extends AnimatorClassicCard implements Hidden
{
    public static final EYBCardData DATA = Register(TheHaunt.class).SetStatus(1, CardRarity.RARE, EYBCardTarget.None);

    public TheHaunt()
    {
        super(DATA);

        int goldBonus = 0;
        AbstractBlight blight = player.getBlight(Haunted.ID);
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

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainGold(magicNumber);

        AbstractBlight blight = player.getBlight(Haunted.ID);
        if (blight != null)
        {
            blight.setCounter(blight.counter + 1);
        }
    }
}
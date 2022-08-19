package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Overheat extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Overheat.class).SetStatus(0, CardRarity.COMMON, EYBCardTarget.None);

    public Overheat()
    {
        super(DATA);

        Initialize(0, 0, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Draw(2);
            GameActions.Bottom.DealDamageAtEndOfTurn(p, p, magicNumber);
            GameActions.Bottom.ModifyAllCopies(cardID, c -> GameUtilities.IncreaseMagicNumber(c, 1, false));
        }
    }
}
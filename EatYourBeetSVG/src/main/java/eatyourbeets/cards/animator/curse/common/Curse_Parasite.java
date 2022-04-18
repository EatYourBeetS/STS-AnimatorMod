package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Parasite extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Parasite.class)
            .SetCurse(3, EYBCardTarget.None, false);

    public Curse_Parasite()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() ->
        {
            final AbstractCard c = GameUtilities.GetMasterDeckInstance(uuid);
            if (c != null)
            {
                player.masterDeck.removeCard(c);
                player.decreaseMaxHealth(magicNumber);
            }
        });
    }
}
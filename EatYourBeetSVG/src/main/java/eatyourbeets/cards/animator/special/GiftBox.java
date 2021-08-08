package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class GiftBox extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GiftBox.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public GiftBox()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetUpgrade(0, 0, 2);
        SetAffinity_Star(1, 0, 0);

        cropPortrait = false;
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, 999, false)
        .SetOptions(false, false, true);

        ArrayList<AbstractCard> cards = GameUtilities.GetAvailableCards();
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.MakeCardInHand(GameUtilities.GetRandomElement(cards))
            .SetUpgrade(false, true);
        }
    }
}
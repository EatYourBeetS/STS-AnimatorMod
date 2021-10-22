package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
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

        Initialize(0, 0, 5, 12);
        SetUpgrade(0, 0, 2, 4);
        SetAffinity_Star(1, 0, 0);

        SetPurge(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        final ArrayList<AbstractCard> cards = GameUtilities.GetAvailableCards();
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(GameUtilities.GetRandomElement(cards))
            .SetUpgrade(false, true)
            .SetDuration(0.01f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        //TODO: Make this into an actual card and not something for testing
        for (Affinity af : Affinity.Basic()) {
            CombatStats.Affinities.AddAffinity(af, 99);
        }
    }
}
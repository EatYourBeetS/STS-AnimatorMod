package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class TsurugaMeisai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TsurugaMeisai.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public TsurugaMeisai()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards != null && cards.size() > 0)
            {
                AbstractCard card = cards.get(0);
                if (card.type == CardType.ATTACK)
                {
                    GameActions.Bottom.MakeCardInDrawPile(card).SetUpgrade(upgraded, true);
                    GameActions.Bottom.MakeCardInDrawPile(card).SetUpgrade(upgraded, true);
                }
            }
        });
    }
}
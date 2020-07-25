package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class HiedaNoAkyuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiedaNoAkyuu.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public HiedaNoAkyuu()
    {
        super(DATA);

        Initialize(0, 0, 5, 0);

        SetEthereal(true);
        SetExhaust(true);
        SetCostUpgrade(-1);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.MoveCards(player.drawPile, player.discardPile);
        GameActions.Bottom.MoveCards(player.hand, player.discardPile);

        GameActions.Bottom.SelectFromPile(name, magicNumber, player.discardPile)
                .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(magicNumber))
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        GameActions.Top.MoveCard(card, player.drawPile);
                    }
                });
    }
}


package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Tet extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tet.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Tet()
    {
        super(DATA);

        Initialize(0, 0);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ShuffleFromDiscardPile();
        DiscardFromDrawPile();
    }

    private void DiscardFromDrawPile()
    {
        GameActions.Top.SelectFromPile(name, 1, player.hand, player.drawPile)
        .SetMessage(GR.Common.Strings.GridSelection.Discard)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.discardPile);
            }
        });
    }

    private void ShuffleFromDiscardPile()
    {
        GameActions.Top.SelectFromPile(name, 1, player.discardPile)
        .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(1))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.drawPile)
                .AddCallback(c -> JavaUtilities.ChangeIndex(c, player.drawPile.group, player.drawPile.size() - 3));
            }
        });
    }
}
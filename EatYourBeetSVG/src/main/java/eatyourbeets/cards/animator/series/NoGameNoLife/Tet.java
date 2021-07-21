package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Tet extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tet.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tet()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Green(2);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromPile(name, 1, player.hand, player.drawPile)
        .SetMessage(GR.Common.Strings.GridSelection.Discard)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.discardPile);
            }
        });

        GameActions.Bottom.SelectFromPile(name, 1, player.discardPile)
        .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(1))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.drawPile)
                .SetDestination((list, c, index) ->
                {
                    index += rng.random(list.size() / 2);
                    index = Math.max(0, Math.min(list.size(), list.size() - index));
                    list.add(index, c);
                });
            }
        });
    }
}
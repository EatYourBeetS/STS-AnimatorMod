package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TupleT2;

public class EndlessResearch extends UnnamedCard
{
    public static final EYBCardData DATA = Register(EndlessResearch.class)
            .SetSkill(3, CardRarity.RARE);

    protected boolean shuffledOnce;

    public EndlessResearch()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        shuffledOnce = false;
        ExecuteAction(m);
    }

    private void ExecuteAction(AbstractMonster m)
    {
        final int cardsInDrawPile = player.drawPile.size();
        if (cardsInDrawPile < magicNumber)
        {
            if (!shuffledOnce && player.discardPile.size() > (magicNumber - cardsInDrawPile))
            {
                GameActions.Top.Callback(m, (enemy, __) -> ExecuteAction(enemy));
                GameActions.Top.Add(new EmptyDeckShuffleAction());
                shuffledOnce = true;
                return;
            }
            else
            {
                return;
            }
        }

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < magicNumber; i++)
        {
            group.addToTop(player.drawPile.getNCardFromTop(i));
        }

        final TupleT2<CardGroup, AbstractMonster> info = new TupleT2<>(group, m);
        GameActions.Bottom.SelectFromPile(name, 1, group)
        .AddCallback(info, (pair, selected) ->
        {
            for (AbstractCard c : pair.V1.group)
            {
                if (selected.contains(c))
                {
                    GameActions.Top.PlayCard(c, player.drawPile, pair.V2);

                    if (c.costForTurn == 0)
                    {
                        GameActions.Bottom.Callback(pair.V2, (enemy, __) -> ExecuteAction(enemy));
                    }
                }
                else
                {
                    GameActions.Top.MoveCard(c, player.drawPile, player.discardPile);
                }
            }
        });
    }
}
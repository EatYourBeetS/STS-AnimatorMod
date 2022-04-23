package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Tet extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tet.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tet()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.hand, player.drawPile)
        .SetMessage(GR.Common.Strings.GridSelection.Discard)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.discardPile);
            }
        });
        GameActions.Bottom.StackPower(new TetPower(p, magicNumber));
    }

    public static class TetPower extends AnimatorPower
    {
        public TetPower(AbstractCreature owner, int amount)
        {
            super(owner, Tet.DATA);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.SelectFromPile(name, amount, player.discardPile)
            .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(amount))
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
            RemovePower();
        }
    }
}
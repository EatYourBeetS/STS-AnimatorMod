package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Retrace extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Retrace.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Retrace()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 0, 1);
    }

    @Override
    protected float GetInitialBlock()
    {
        float block = super.GetInitialBlock();
        for (AbstractCard c : player.hand.group)
        {
            if (CanReshuffle(c))
            {
                block += magicNumber;
            }
        }

        return block;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.MoveCards(player.hand, player.drawPile)
        .SetFilter(this::CanReshuffle)
        .SetDestination(CardSelection.Random);
    }

    private boolean CanReshuffle(AbstractCard c)
    {
        return c.type == CardType.ATTACK && !c.uuid.equals(uuid);
    }
}
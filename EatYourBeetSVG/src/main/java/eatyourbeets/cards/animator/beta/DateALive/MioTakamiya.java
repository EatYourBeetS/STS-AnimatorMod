package eatyourbeets.cards.animator.beta.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MioTakamiya extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(MioTakamiya.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new ShidoItsuka(), true);
    }

    public MioTakamiya()
    {
        super(DATA);

        Initialize(0, 18, 0);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        for (int i=0; i<3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
            .SetUpgrade(upgraded, true);
        }

        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        MoveShidosAndSetZeroCost(p.exhaustPile, p.hand);
        MoveShidosAndSetZeroCost(p.drawPile, p.hand);
        MoveShidosAndSetZeroCost(p.discardPile, p.hand);
        MoveShidosAndSetZeroCost(p.hand, p.hand);
    }

    private void MoveShidosAndSetZeroCost(CardGroup source, CardGroup destination)
    {
        for (AbstractCard card : source.group)
        {
            if (card.cardID.equals(ShidoItsuka.DATA.ID))
            {
                card.setCostForTurn(0);

                if (!source.type.equals(destination.type))
                {
                    GameActions.Bottom.MoveCard(card, source, destination);
                }
            }
        }
    }
}
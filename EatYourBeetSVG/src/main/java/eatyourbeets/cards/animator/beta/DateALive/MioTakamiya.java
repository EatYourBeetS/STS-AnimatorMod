package eatyourbeets.cards.animator.beta.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MioTakamiya extends AnimatorCard_UltraRare implements StartupCard
{
    public static final EYBCardData DATA = Register(MioTakamiya.class).SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new ShidoItsuka(), true);
    }

    public MioTakamiya()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.MoveCards(p.exhaustPile, p.hand).SetFilter(c -> ShidoItsuka.DATA.ID.equals(c.cardID));
        GameActions.Bottom.MoveCards(p.discardPile, p.hand).SetFilter(c -> ShidoItsuka.DATA.ID.equals(c.cardID));
        GameActions.Bottom.MoveCards(p.drawPile, p.hand).SetFilter(c -> ShidoItsuka.DATA.ID.equals(c.cardID));
        GameActions.Last.Callback(() ->
        {
            for (AbstractCard card : player.hand.group)
            {
                if (card.cardID.equals(ShidoItsuka.DATA.ID))
                {
                    card.setCostForTurn(0);
                }
            }
        });
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        final ShidoItsuka shido = new ShidoItsuka();
        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(shido)
            .SetUpgrade(upgraded, true);
        }

        return true;
    }
}
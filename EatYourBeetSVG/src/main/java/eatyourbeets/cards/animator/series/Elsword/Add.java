package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Add extends AnimatorCard
{
    public static final String ID = Register(Add.class.getSimpleName(), EYBCardBadge.Synergy);

    // TODO: Redesign this card
    public Add()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0);

        SetExhaust(true);
        SetEthereal(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
        .AddCallback(this::OnCardChosen);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.Add(new DoubleEnergyAction());
        }
    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            AbstractCard c = cards.get(0);
            AbstractPlayer p = AbstractDungeon.player;

            CardGroup cardGroup = null;
            if (p.hand.contains(c))
            {
                cardGroup = p.hand;
            }
            else if (p.drawPile.contains(c))
            {
                cardGroup = p.drawPile;
            }
            else if (p.discardPile.contains(c))
            {
                cardGroup = p.discardPile;
            }

            if (cardGroup != null)
            {
                GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(cardGroup, this::OrbChosen));
            }
        }
    }

    private void OrbChosen(Object state, ArrayList<AbstractCard> chosen)
    {
        CardGroup cardGroup = JavaUtilities.SafeCast(state, CardGroup.class);
        if (cardGroup != null && chosen != null && chosen.size() == 1)
        {
            switch (cardGroup.type)
            {
                case DRAW_PILE:
                    GameActions.Bottom.MakeCardInDrawPile(chosen.get(0));
                    break;

                case HAND:
                    GameActions.Bottom.MakeCardInHand(chosen.get(0));
                    break;

                case DISCARD_PILE:
                    GameActions.Bottom.MakeCardInDiscardPile(chosen.get(0));
                    break;

                case MASTER_DECK:
                    break;
                case EXHAUST_PILE:
                    break;
                case CARD_POOL:
                    break;
                case UNSPECIFIED:
                    break;
            }
        }
    }
}
package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Add extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Add.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.InitializePreview(new Crystallize(), false);
    }

    public Add()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new EnergizedBluePower(p, 2));
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));

        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Crystallize());
        }

        if (HasSynergy())
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
            .AddCallback(this::OnCardChosen);
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
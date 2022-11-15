package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import eatyourbeets.cards.animatorClassic.special.OrbCore;
import eatyourbeets.cards.animatorClassic.status.Crystallize;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Add extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Add.class).SetSeriesFromClassPackage().SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Crystallize(), false);
        for (OrbCore core : OrbCore.GetAllCores())
        {
            DATA.AddPreview(core, false);
        }
    }

    public Add()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetExhaust(true);
        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EnergizedBluePower(p, 2));
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing)
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
            .AddCallback(this::OnCardChosen);
        }

        GameActions.Bottom.MakeCardInDrawPile(new Crystallize()).Repeat(secondaryValue);
    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            AbstractCard c = cards.get(0);
            CardGroup cardGroup = null;
            if (player.hand.contains(c))
            {
                cardGroup = player.hand;
            }
            else if (player.drawPile.contains(c))
            {
                cardGroup = player.drawPile;
            }
            else if (player.discardPile.contains(c))
            {
                cardGroup = player.discardPile;
            }

            if (cardGroup != null)
            {
                GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(cardGroup, this::OrbChosen));
            }
        }
    }

    private void OrbChosen(CardGroup cardGroup, ArrayList<AbstractCard> chosen)
    {
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
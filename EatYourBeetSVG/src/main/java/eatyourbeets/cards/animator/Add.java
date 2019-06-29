package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ChooseFromAnyPileAction;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animator.OrbCore_AbstractPower;

import java.util.ArrayList;

public class Add extends AnimatorCard
{
    public static final String ID = CreateFullID(Add.class.getSimpleName());

    public Add()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 12);

        this.exhaust = true;
        this.isEthereal = true;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.AddToBottom(new ChooseFromAnyPileAction(1, this::OnCardChosen, this, ExhaustAction.TEXT[0]));

//        if (p.drawPile.size() > 0)
//        {
//            //GameActionsHelper.AddToBottom(new ExhaustFromPileAction(1, false, p.drawPile));
//
//            CardGroup cores = OrbCore_AbstractPower.CreateCoresGroup(true);
//            GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, cores, this::OrbChosen, this, "", true));
//        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.isEthereal = false;
        }
    }

    private void OnCardChosen(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null && cards.size() == 1)
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
                GameActionsHelper.ExhaustCard(c, cardGroup);

                CardGroup cores = OrbCore_AbstractPower.CreateCoresGroup(true);
                GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false,
                        cores, this::OrbChosen, cardGroup, "", true));
            }
        }
    }

    private void OrbChosen(Object state, ArrayList<AbstractCard> chosen)
    {
        if (state instanceof CardGroup && chosen != null && chosen.size() == 1)
        {
            CardGroup cardGroup = (CardGroup) state;
            switch (cardGroup.type)
            {
                case DRAW_PILE:
                    GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(chosen.get(0), 1, true, true));
                    break;

                case HAND:
                    GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(chosen.get(0), 1));
                    break;

                case DISCARD_PILE:
                    GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(chosen.get(0), 1));
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
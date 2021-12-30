package pinacolada.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

public class CardPoolPanelItem extends PCLTopPanelItem
{
    public static final String ID = CreateFullID(CardPoolPanelItem.class);
    protected CardGroup cardGroup;

    public CardPoolPanelItem() {
        super(GR.PCL.Images.CardPool, ID);
        SetTooltip(new PCLCardTooltip(GR.PCL.Strings.Misc.ViewCardPool, GR.PCL.Strings.Misc.ViewCardPoolDescription));
    }

    @Override
    protected void onClick() {
        super.onClick();

        GR.UI.CardsScreen.Open(GetAllCards());
    }

    protected CardGroup GetAllCards() {
        if (cardGroup == null) {
            cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        }
        if (PCLGameUtilities.GetTotalCardsInPlay() != cardGroup.size()) {
            cardGroup.clear();
            for (CardGroup cg: PCLGameUtilities.GetSourceCardPools()) {
                for (AbstractCard c : cg.group) {
                    cardGroup.addToTop(c);
                }
            }
        }

        return cardGroup;
    }
}

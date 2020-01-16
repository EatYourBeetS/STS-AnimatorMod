package eatyourbeets.ui.screens.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

public class SeriesSelectionItem
{
    public int size;
    public boolean promoted;
    public AnimatorCard card;
    public AnimatorLoadout loadout;

    public static SeriesSelectionItem TryCreate(AnimatorLoadout loadout)
    {
        if (loadout != null)
        {
            int size = loadout.GetNonColorlessCards().size();
            if (size > 0)
            {
                return new SeriesSelectionItem(loadout);
            }
        }

        return null;
    }

    public SeriesSelectionItem(AnimatorLoadout loadout)
    {
        this.loadout = loadout;
        this.size = loadout.GetNonColorlessCards().size();
        this.promoted = false;
        this.card = null;
    }

    public void Promote()
    {
        if (card != null)
        {
            throw new RuntimeException("Can not promote a card that has already been built.");
        }

        this.promoted = true;
    }

    public AbstractCard BuildCard()
    {
        AbstractCard temp = CardLibrary.getCard(loadout.GetSymbolicCardID());
        AnimatorCardBuilder builder = new AnimatorCardBuilder(String.valueOf(loadout.ID)).SetImage(temp.assetUrl);

        if (promoted)
        {
            card = builder
            .SetText(loadout.Name, "Contains " + size + " cards. NL Whenever you obtain NL cards from this series, NL gain 1 Max. HP", "")
            .SetProperties(temp.type, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE).Build();
            card.retain = true;
        }
        else if (loadout.IsBeta)
        {
            card = builder
            .SetText(loadout.Name, "#yBeta[] #ySeries[]. NL Contains " + size + " cards. NL (Not really, this NL is just an example)", "")
            .SetProperties(temp.type, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE).Build();
            card.retain = false;
        }
        else
        {
            card = builder
            .SetText(loadout.Name, "Contains " + size + " cards.", "")
            .SetProperties(temp.type, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE).Build();
            card.retain = false;
        }

        return card;
    }
}

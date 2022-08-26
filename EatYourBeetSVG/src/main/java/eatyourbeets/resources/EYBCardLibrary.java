package eatyourbeets.resources;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameUtilities;

public class EYBCardLibrary
{
    public String GetBaseID(String cardID)
    {
        if (cardID.startsWith(GR.Animator.Prefix))
        {
            if (cardID.startsWith(GR.AnimatorClassic.Prefix))
            {
                return GR.AnimatorClassic.CardLibrary.GetBaseID(cardID);
            }

            return GR.Animator.CardLibrary.GetBaseID(cardID);
        }

        if (cardID.startsWith(GR.Unnamed.Prefix))
        {
            return GR.Unnamed.CardLibrary.GetBaseID(cardID);
        }

        return cardID;
    }

    public EYBCardData GetCardData(String cardID)
    {
        if (cardID.startsWith(GR.Animator.Prefix))
        {
            if (cardID.startsWith(GR.AnimatorClassic.Prefix))
            {
                return GR.AnimatorClassic.CardLibrary.GetCardData(cardID);
            }

            return GR.Animator.CardLibrary.GetCardData(cardID);
        }

        if (cardID.startsWith(GR.Unnamed.Prefix))
        {
            return GR.Unnamed.CardLibrary.GetCardData(cardID);
        }

        return null;
    }

    public EYBCardData GetCardData(AbstractResources resources, String cardID)
    {
        return resources.CardLibrary.GetCardData(cardID);
    }

    public EYBCardData GetCardData(AbstractPlayer.PlayerClass playerClass, String cardID)
    {
        if (playerClass == GR.Animator.PlayerClass)
        {
            return GR.Animator.CardLibrary.GetCardData(cardID);
        }
        if (playerClass == GR.AnimatorClassic.PlayerClass)
        {
            return GR.AnimatorClassic.CardLibrary.GetCardData(cardID);
        }
        if (playerClass == GR.Unnamed.PlayerClass)
        {
            return GR.Unnamed.CardLibrary.GetCardData(cardID);
        }

        return null;
    }

    public EYBCardData GetCurrentClassCardData(String cardID)
    {
        return GetCardData(GameUtilities.GetPlayerClass(), GetBaseID(cardID));
    }

    public AbstractCard GetCurrentClassCard(String cardID, boolean upgrade)
    {
        return GetCard(GameUtilities.GetPlayerClass(), cardID, upgrade);
    }

    public AbstractCard GetCard(AbstractPlayer.PlayerClass playerClass, String cardID, boolean upgrade)
    {
        final EYBCardData data = GetCardData(playerClass, GetBaseID(cardID));
        return data != null ? data.CreateNewInstance(upgrade) : CardLibrary.getCopy(cardID, upgrade ? 1 : 0, 0);
    }

    public AbstractCard GetCard(AbstractResources resources, String cardID, boolean upgrade)
    {
        final EYBCardData data = GetCardData(resources, GetBaseID(cardID));
        return data != null ? data.CreateNewInstance(upgrade) : CardLibrary.getCopy(cardID, upgrade ? 1 : 0, 0);
    }

    public AbstractCard GetCard(String cardID, boolean upgrade)
    {
        final EYBCardData data = GetCardData(cardID);
        return data != null ? data.CreateNewInstance(upgrade) : CardLibrary.getCopy(cardID, upgrade ? 1 : 0, 0);
    }

    public AbstractCard TryReplace(AbstractCard card)
    {
        final EYBCardData data = GetCurrentClassCardData(card.cardID);
        return (data == null || data.IsCard(card)) ? card : data.CreateNewInstance(card.upgraded);
    }

    public boolean SameBaseID(String cardID, String otherID)
    {
        return GetBaseID(cardID).equals(GetBaseID(otherID));
    }
}

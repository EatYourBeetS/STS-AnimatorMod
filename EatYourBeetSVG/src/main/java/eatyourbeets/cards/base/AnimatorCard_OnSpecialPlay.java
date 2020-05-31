package eatyourbeets.cards.base;

public abstract class AnimatorCard_OnSpecialPlay extends AnimatorCard
{
    protected AnimatorCard_OnSpecialPlay(EYBCardData cardData)
    {
        super(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget());
    }

    protected AnimatorCard_OnSpecialPlay(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    public void OnSpecialPlay() {

    }
}
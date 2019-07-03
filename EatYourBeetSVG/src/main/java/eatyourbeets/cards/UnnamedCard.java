package eatyourbeets.cards;

import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.resources.Resources_Animator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

public abstract class UnnamedCard extends EYBCard
{
    protected static final Logger logger = LogManager.getLogger(UnnamedCard.class.getName());

    public static String CreateFullID(String cardID)
    {
        return "unnamed:" + cardID;
    }

    protected UnnamedCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(id, cost, type, AbstractEnums.Cards.THE_UNNAMED, rarity, target);
    }

    protected UnnamedCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target, boolean usePlaceholder)
    {
        this(Resources_Animator.GetCardStrings(id), id,
                Resources_Animator.GetCardImage(usePlaceholder? "unnamed:Placeholder" : id),
                cost, type, AbstractEnums.Cards.THE_UNNAMED, rarity, target);
    }

    protected UnnamedCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(Resources_Animator.GetCardStrings(id), id, Resources_Animator.GetCardImage(id), cost, type, color, rarity, target);
    }

    protected UnnamedCard(CardStrings strings, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(strings, id, imagePath, cost, type, color, rarity, target);
    }
}
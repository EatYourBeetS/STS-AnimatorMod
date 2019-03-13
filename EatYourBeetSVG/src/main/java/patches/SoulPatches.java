package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = Soul.class, method = "discard", paramtypez = {AbstractCard.class, boolean.class})
public class SoulPatches
{
    public static void Postfix(Soul soul, AbstractCard card, boolean visualOnly)
    {
        if (card != null && card.tags.contains(AbstractEnums.CardTags.LOYAL) && !visualOnly)
        {
            soul.isReadyForReuse = true;
            AbstractDungeon.player.discardPile.moveToDeck(card, true);
        }
    }
}
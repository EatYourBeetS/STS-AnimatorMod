package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;

@SpirePatch(clz = Soul.class, method = "discard", paramtypez = {AbstractCard.class, boolean.class})
public class Soul_Discard
{
    @SpirePostfixPatch
    public static void Postfix(Soul soul, AbstractCard card, boolean visualOnly)
    {
        if (card != null)
        {
            if (card.tags.contains(GR.Enums.CardTags.LOYAL)) // it also needs to work when visualOnly
            {
                soul.isReadyForReuse = true;
                AbstractDungeon.player.discardPile.moveToDeck(card, true);
            }
            else if (card.tags.contains(GR.Enums.CardTags.PURGING) && !visualOnly)
            {
                soul.isReadyForReuse = true;
                AbstractDungeon.player.discardPile.removeCard(card);
                card.tags.remove(GR.Enums.CardTags.PURGING);
            }
        }
    }
}
package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;
import java.util.Map;

public class TipHelperPatches
{
    //private static Field<Boolean> RenderedTipThisFrame = Utilities.GetPrivateField("renderedTipThisFrame", TipHelper.class);
    private static Field<Boolean> IsCard = Utilities.GetPrivateField("isCard", TipHelper.class);
    private static Field<AbstractCard> Card = Utilities.GetPrivateField("card", TipHelper.class);

    @SpirePatch(clz= TipHelper.class, method="renderBox")
    public static class TipHelperPatches_renderTipForCard
    {
        @SpirePrefixPatch
        public static void Method(SpriteBatch sb, String word, float x, float y)
        {
            if (IsCard.Get(null))
            {
                Map<String, String> keyword = AbstractResources.GetDynamicKeyword(word);
                if (keyword != null)
                {
                    AbstractCard card = Card.Get(null);
                    if (card != null)
                    {
                        String text = keyword.get(card.cardID);
                        GameDictionary.keywords.replace(word, text);
                    }
                }
            }
        }
    }

//    @SpirePatch(clz= TipHelper.class, method="renderTipForCard")
//    public static class TipHelperPatches_renderTipForCard
//    {
//        @SpirePrefixPatch
//        public static void Method(AbstractCard c, SpriteBatch sb, ArrayList<String> keywords)
//        {
//            if (!RenderedTipThisFrame.Get(null))
//            {
//                AbstractResources.GetDynamicKeyword(keywords)
//
//                if (c instanceof Spellcaster)
//                {
//                    GameDictionary.keywords.replace()
//                }
//                else if (c instanceof MartialArtist)
//                {
//
//                }
//            }
//        }
//    }
}

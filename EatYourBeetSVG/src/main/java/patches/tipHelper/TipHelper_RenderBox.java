package patches.tipHelper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

import java.util.Map;

@SpirePatch(clz= TipHelper.class, method="renderBox")
public class TipHelper_RenderBox
{
    private static final FieldInfo<Boolean> IsCard = JavaUtilities.GetPrivateField("isCard", TipHelper.class);
    private static final FieldInfo<AbstractCard> Card = JavaUtilities.GetPrivateField("card", TipHelper.class);

    @SpirePrefixPatch
    public static void Method(SpriteBatch sb, String word, float x, float y)
    {
        if (IsCard.Get(null))
        {
            Map<String, String> keyword = GR.GetDynamicKeyword(word);
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

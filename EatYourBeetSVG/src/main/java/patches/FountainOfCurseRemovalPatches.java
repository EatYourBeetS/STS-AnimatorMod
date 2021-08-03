package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

@SpirePatch(clz = FountainOfCurseRemoval.class, method = "buttonEffect", paramtypez = {int.class})
public class FountainOfCurseRemovalPatches
{
    private static final FieldInfo<Integer> _screenNum = JUtils.GetField("screenNum", FountainOfCurseRemoval.class);

    @SpirePrefixPatch
    public static void Prefix(FountainOfCurseRemoval __instance, int button)
    {
        if (button == 0 && _screenNum.Get(__instance) == 0)
        {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (c.type == AbstractCard.CardType.CURSE && c.rarity == AbstractCard.CardRarity.SPECIAL)
                {
                    c.inBottleFlame = true;
                }
            }
        }
    }

    @SpirePostfixPatch
    public static void Postfix(FountainOfCurseRemoval __instance, int button)
    {
        if (button == 0 && _screenNum.Get(__instance) == 0)
        {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (c.type == AbstractCard.CardType.CURSE && c.rarity == AbstractCard.CardRarity.SPECIAL)
                {
                    c.inBottleFlame = false;
                }
            }
        }
    }
}

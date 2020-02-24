package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import eatyourbeets.resources.GR;

@SpirePatch(clz = Soul.class, method = "obtain", paramtypez = {AbstractCard.class})
public class Soul_Obtain
{
    public static void Postfix(Soul soul, AbstractCard card)
    {
        GR.Animator.Dungeon.OnCardObtained(card);
    }
}
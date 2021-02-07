package patches.abstractCard;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.modifiers.CostModifier;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class AbstractCard_Fields
{
    public static SpireField<CostModifier> costModifier = new SpireField<>(() -> null);
}
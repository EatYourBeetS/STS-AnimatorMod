package patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.modifiers.BlockModifier;
import eatyourbeets.cards.base.modifiers.CostModifier;
import eatyourbeets.cards.base.modifiers.DamageModifier;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class AbstractCard_Fields
{
    public static SpireField<CostModifier> costModifier = new SpireField<>(() -> null);
    public static SpireField<DamageModifier> damageModifier = new SpireField<>(() -> null);
    public static SpireField<BlockModifier> blockModifier = new SpireField<>(() -> null);
}
package patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class AbstractCard_Fields
{
    public static final SpireField<CostModifiers> costModifiers = new SpireField<>(() -> null);
    public static final SpireField<DamageModifiers> damageModifiers = new SpireField<>(() -> null);
    public static final SpireField<BlockModifiers> blockModifiers = new SpireField<>(() -> null);
}
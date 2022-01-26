package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class GenericEffect
{
    protected static final String SEPARATOR = "|";

    private static final HashMap<String, Class<? extends GenericEffect>> EFFECT_MAP = new HashMap<>();

    public static String Register(Class<? extends GenericEffect> type) {
        String id = GR.PCL.CreateID(type.getSimpleName());
        EFFECT_MAP.put(id, type);
        return id;
    }

    public static GenericEffect Get(String serializedString) {
        String[] content = PCLJUtils.SplitString(SEPARATOR, serializedString);
        Constructor<? extends GenericEffect> c = PCLJUtils.TryGetConstructor(EFFECT_MAP.get(content[0]), String[].class);
        if (c != null) {
            try {
                return c.newInstance((Object) content);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String effectID;
    public String entityID;
    public PCLCardTarget target;
    public PCLCardTooltip tooltip;
    public int amount;

    public GenericEffect() {
        target = PCLCardTarget.None;
    }

    public GenericEffect(String effectID, String entityID, PCLCardTooltip tooltip, PCLCardTarget target, int amount) {
        this.effectID = effectID;
        this.entityID = entityID;
        this.tooltip = tooltip;
        this.target = target;
        this.amount = amount;
    }

    public final String Serialize() {
        return PCLJUtils.JoinStrings(SEPARATOR, new String[] {
           effectID,
           entityID,
           tooltip.id,
           target.name(),
           String.valueOf(amount)
        });
    }

    public abstract String GetText();
    public abstract void Use(PCLCard card, AbstractPlayer p, AbstractMonster m);
}

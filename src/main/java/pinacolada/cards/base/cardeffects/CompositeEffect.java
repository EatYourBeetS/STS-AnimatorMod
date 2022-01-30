package pinacolada.cards.base.cardeffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class CompositeEffect extends GenericEffect {
    protected final ArrayList<GenericEffect> effects;

    public CompositeEffect(GenericEffect... effects) {
        this.target = PCLJUtils.Max(effects, effect -> effect.target);
        this.effects = new ArrayList<>(Arrays.asList(effects));
        this.effectID = GenericEffect.JoinEntityIDs(effects, effect -> effect.entityID);
    }

    @Override
    public String GetText() {
        return GenericEffect.JoinEffectTexts(effects);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m) {
        for (GenericEffect effect : effects) {
            effect.Use(card, p, m);
        }
    }
}

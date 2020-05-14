package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.AnimatorCard;

public class RetainMod extends AbstractCardModifier {

    private boolean alreadyRetaining = false;

    public AbstractCardModifier makeCopy() {
        return new RetainMod();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.selfRetain) {
            alreadyRetaining = true;
        }
        card.selfRetain = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        if (!alreadyRetaining) {
            card.selfRetain = false;
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!alreadyRetaining && !(card instanceof AnimatorCard)) {
            return "Retain. NL " + rawDescription;
        }
        return rawDescription;
    }
}

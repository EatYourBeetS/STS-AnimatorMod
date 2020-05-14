package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;

public class RetainMod extends AbstractCardModifier {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GR.Animator.CreateID("CardMods"));
    public static final String[] TEXT = uiStrings.TEXT;

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
            return TEXT[2] + rawDescription;
        }
        return rawDescription;
    }
}

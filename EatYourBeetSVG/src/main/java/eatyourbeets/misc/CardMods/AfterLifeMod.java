package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.cards.animator.beta.AngelBeats.EriShiina;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class AfterLifeMod extends AbstractCardModifier
{

    public static final String ID = GR.Animator.CreateID("Afterlife");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GR.Animator.CreateID("CardMods"));
    public static final String[] TEXT = uiStrings.TEXT;

    public AbstractCardModifier makeCopy()
    {
        return new AfterLifeMod();
    }

    @Override
    public String identifier(AbstractCard card)
    {
        return ID;
    }

    @Override
    public void onExhausted(AbstractCard card) {
       // AbstractCard clone = card.makeStatEquivalentCopy();
        //clone.uuid = card.uuid;
        CombatStats.ControlPile.Add(card)
                .OnUpdate(c ->
                {
                    boolean originalExists = false;
                    for (AbstractCard cardToCheck : player.exhaustPile.group) {
                        if (cardToCheck.uuid == c.card.uuid) {
                            originalExists = true;
                        }
                    }
                    //System.out.println(originalExists);
                    if (!originalExists) {
                        c.Delete();
                    } else {
                        c.SetEnabled(!CombatStats.HasActivatedSemiLimited("<AFTERLIFE>"));
                    }
                });
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return ((card instanceof AnimatorCard) ? TEXT[0] : TEXT[1]) + rawDescription;
    }
}

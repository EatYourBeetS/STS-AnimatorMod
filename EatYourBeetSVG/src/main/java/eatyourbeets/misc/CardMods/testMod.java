package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.TrueGrit;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class testMod extends AbstractCardModifier
{

    public static final String ID = GR.Animator.CreateID("Afterlife");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GR.Animator.CreateID("CardMods"));
    public static final String[] TEXT = uiStrings.TEXT;

    public AbstractCardModifier makeCopy()
    {
        return new testMod();
    }

    @Override
    public String identifier(AbstractCard card)
    {
        return ID;
    }

    @Override
    public void onExhausted(AbstractCard card) {
        CombatStats.ControlPile.Add(card, player.exhaustPile, true)
                .OnUpdate(c ->
                {
                    boolean originalExists = false;
                    for (AbstractCard cardToCheck : player.exhaustPile.group) {
                        if (cardToCheck.uuid == c.card.uuid) {
                            originalExists = true;
                        }
                    }
                    if (!originalExists) {
                        c.Delete();
                    } else {
                        c.SetEnabled(!CombatStats.HasActivatedSemiLimited("<AFTERLIFE>"));
                    }
                })
                .OnSelect(c ->
                {
                    CombatStats.TryActivateSemiLimited("<AFTERLIFE>");
                    System.out.println("use altered");
                    GameActions.Top.MakeCardInHand(new TrueGrit());
                    c.Delete();
                });
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return ((card instanceof AnimatorCard) ? TEXT[0] : TEXT[1]) + rawDescription;
    }
}

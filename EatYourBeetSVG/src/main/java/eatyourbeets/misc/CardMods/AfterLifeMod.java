package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.cardManipulation.ModifyAllInstances;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.ControllableCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;

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
    public void onExhausted(AbstractCard card)
    {
        AfterlifeAddToControlPile(card);
    }

    public static void AfterlifeAddToControlPile(AbstractCard card)
    {
        CombatStats.ControlPile.Add(card)
                .OnUpdate(control ->
                {
                    if (!AbstractDungeon.player.exhaustPile.contains(control.card))
                    {
                        control.Delete();
                    }
                })
                .OnSelect(control ->
                {
                    AbstractCard cardToPurge = TryPurgeRandomCard();
                    if (CombatStats.ControlPile.IsHovering() && control.card.canUse(AbstractDungeon.player, null) && cardToPurge != null)
                    {
                        GameActions.Bottom.SelectCreature(control.card).AddCallback(control, (state, creature) ->
                        {
                            //Put this here so the cost is only paid upon successful completion of the selectCreature action
                            AbstractCard copy = cardToPurge.makeStatEquivalentCopy();
                            GameEffects.List.ShowCardBriefly(copy);
                            GameEffects.List.Add(new ExhaustCardEffect(copy));
                            AbstractDungeon.player.exhaustPile.removeCard(cardToPurge);

                            ControllableCard c = (ControllableCard) state;

                            GameActions.Bottom.PlayCard(c.card, AbstractDungeon.player.exhaustPile, JavaUtilities.SafeCast(creature, AbstractMonster.class))
                                    .SpendEnergy(true)
                                    .AddCallback(c, (temp, __) -> ((ControllableCard)temp).Delete());

                            ModifyAllInstances action = GameActions.Bottom.ModifyAllInstances(c.card.uuid, AbstractCard::stopGlowing);
                            if (c.card.exhaust)
                            {
                                c.card.exhaust = false;
                                action.AddCallback(playedCard -> playedCard.exhaust = true);
                            }
                        });
                    }
                });
    }

    private static AbstractCard TryPurgeRandomCard()
    {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractCard card = JavaUtilities.GetRandomElement(JavaUtilities.Filter(player.exhaustPile.group, c -> !CardModifierManager.hasModifier(c, AfterLifeMod.ID)));
        if (card != null)
        {
            return card;
        }
        else
        {
            GameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, TEXT[2], true));
            return null;
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return ((card instanceof AnimatorCard) ? TEXT[0] : TEXT[1]) + rawDescription;
    }
}

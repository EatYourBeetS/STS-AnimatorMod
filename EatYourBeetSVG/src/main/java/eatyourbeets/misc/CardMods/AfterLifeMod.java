package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.cardManipulation.ModifyAllInstances;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;

public class AfterLifeMod extends AbstractCardModifier
{
    public static final String ID = GR.Animator.CreateID("Afterlife");
    public static final String[] TEXT = GR.GetUIStrings(GR.Animator.CreateID("CardMods")).TEXT;

    public static void Add(AbstractCard card)
    {
        CardModifierManager.addModifier(card, new AfterLifeMod());
    }

    public static boolean IsAdded(AbstractCard card)
    {
        return CardModifierManager.hasModifier(card, ID);
    }

    @Override
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
            AbstractCard cardToPurge = FindCardToPurge();
            if (CombatStats.ControlPile.IsHovering() && control.card.canUse(AbstractDungeon.player, null) && cardToPurge != null)
            {
                GameActions.Bottom.SelectCreature(control.card).AddCallback(control, (state, creature) ->
                {
                    //Put this here so the cost is only paid upon successful completion of the selectCreature action
                    AbstractCard copy = cardToPurge.makeStatEquivalentCopy();
                    GameEffects.List.ShowCardBriefly(copy);
                    GameEffects.List.Add(new ExhaustCardEffect(copy));
                    AbstractDungeon.player.exhaustPile.removeCard(cardToPurge);

                    GameActions.Bottom.PlayCard(state.card, AbstractDungeon.player.exhaustPile, JavaUtilities.SafeCast(creature, AbstractMonster.class))
                    .SpendEnergy(true)
                    .AddCallback(state, (temp, __) -> temp.Delete());

                    ModifyAllInstances action = GameActions.Bottom.ModifyAllInstances(state.card.uuid, AbstractCard::stopGlowing);
                    if (state.card.exhaust)
                    {
                        state.card.exhaust = false;
                        action.AddCallback(playedCard -> playedCard.exhaust = true);
                    }
                });
            }
        });
    }

    private static AbstractCard FindCardToPurge()
    {
        final AbstractPlayer player = AbstractDungeon.player;
        final AbstractCard card = JavaUtilities.GetRandomElement(JavaUtilities.Filter(player.exhaustPile.group, c -> !AfterLifeMod.IsAdded(c)));
        if (card == null)
        {
            GameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, TEXT[2], true));
        }

        return card;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return ((card instanceof AnimatorCard) ? TEXT[0] : TEXT[1]) + rawDescription;
    }
}

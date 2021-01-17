package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.cardManipulation.ModifyAllInstances;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.ControllableCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

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
            if (!player.exhaustPile.contains(control.card))
            {
                control.Delete();
            }
        })
        .OnSelect(AfterLifeMod::PlayFromAfterlife);
    }

    public static void PlayFromAfterlife(ControllableCard control)
    {
        GameActions.Bottom.SelectCreature(control.card).AddCallback(control, (state, creature) ->
        {
            //Put this here so the cost is only paid upon successful completion of the selectCreature action
            AbstractCard cardToPurge = JUtils.GetRandomElement(JUtils.Filter(player.exhaustPile.group, AfterLifeMod::CanPurge));
            AbstractCard copy = cardToPurge.makeStatEquivalentCopy();
            GameEffects.List.ShowCardBriefly(copy);
            GameEffects.List.Add(new ExhaustCardEffect(copy));

            GameActions.Bottom.ModifyAllInstances(state.card.uuid, c -> ((EYBCard)c).SetPurge(true));
            GameActions.Bottom.PlayCard(state.card, player.exhaustPile, JUtils.SafeCast(creature, AbstractMonster.class))
                    .SpendEnergy(true);

            ModifyAllInstances action = GameActions.Bottom.ModifyAllInstances(state.card.uuid, AbstractCard::stopGlowing);
            action.AddCallback(state, (temp, __) -> temp.Delete());
            if (state.card.exhaust)
            {
                state.card.exhaust = false;
                action.AddCallback(playedCard -> playedCard.exhaust = true);
            }
        });
    }

    private static boolean CanPurge(AbstractCard card)
    {
        return !AfterLifeMod.IsAdded(card);
    }
}

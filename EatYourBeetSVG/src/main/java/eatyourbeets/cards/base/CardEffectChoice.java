package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class CardEffectChoice
{
    protected final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final ArrayList<AnimatorCardBuilder> effects = new ArrayList<>();
    protected AnimatorCard source;

    public void Initialize(AnimatorCard source)
    {
        Initialize(source, false);
    }

    public void Initialize(AnimatorCard source, boolean clearEffects)
    {
        this.source = source;

        if (clearEffects)
        {
            effects.clear();
            group.clear();
        }
    }

    public boolean TryInitialize(AnimatorCard source)
    {
        if (this.source == null)
        {
            Initialize(source);
            return true;
        }

        return false;
    }

    public CardGroup Build(boolean forceRebuild)
    {
        if (group.isEmpty() || forceRebuild)
        {
            group.clear();

            for (AnimatorCardBuilder b : effects)
            {
                group.addToTop(b.Build());
            }
        }

        return group;
    }

    public AnimatorCardBuilder AddEffect(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUse)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(source, text, false).SetOnUse(onUse);
        effects.add(builder);
        return builder;
    }

    public AnimatorCardBuilder AddEffect(GenericEffect effect)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(source, effect.GetText(), false).SetOnUse(effect::Use);
        if (effect.id != null)
        {
            builder.SetID(effect.id);
        }
        effects.add(builder);
        return builder;
    }

    public SelectFromPile Select(int amount, AbstractMonster m)
    {
        return Select(GameActions.Bottom, Build(false), amount, m);
    }

    public SelectFromPile Select(GameActions gameActions, int amount, AbstractMonster m)
    {
        return Select(gameActions, Build(false), amount, m);
    }

    public SelectFromPile Select(GameActions gameActions, CardGroup group, int amount, AbstractMonster m)
    {
        return (SelectFromPile) gameActions.SelectFromPile(source.name, amount, group)
        .SetOptions(false, false)
        .AddCallback(m, (target, cards) ->
        {
            for (AbstractCard card : cards)
            {
                card.use(AbstractDungeon.player, target);
            }
        });
    }
}

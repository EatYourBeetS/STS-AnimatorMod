package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class CardEffectChoice
{
    protected final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final ArrayList<DynamicCardBuilder> effects = new ArrayList<>();
    protected EYBCard source;

    public boolean ShouldInitialize()
    {
        return source == null;
    }

    public void Initialize(EYBCard source)
    {
        Initialize(source, false);
    }

    public void Initialize(EYBCard source, boolean clearEffects)
    {
        this.source = source;

        if (clearEffects)
        {
            effects.clear();
            group.clear();
        }
    }

    public boolean TryInitialize(EYBCard source)
    {
        if (ShouldInitialize())
        {
            Initialize(source);
            return true;
        }

        return false;
    }

    public CardGroup Build(boolean forceRebuild, AbstractMonster m)
    {
        if (group.isEmpty() || forceRebuild)
        {
            group.clear();

            for (DynamicCardBuilder b : effects)
            {
                final EYBCard c = b.Build();
                if (GameUtilities.IsPlayable(c, m))
                {
                    group.addToTop(c);
                }
            }
        }

        return group;
    }

    public DynamicCardBuilder AddEffect(String text, ActionT3<EYBCard, AbstractPlayer, AbstractMonster> onUse)
    {
        final DynamicCardBuilder builder = ((source instanceof AnimatorCard) ?
        new AnimatorCardBuilder(source.cardID, source, text, false) : (source instanceof AnimatorClassicCard) ?
        new AnimatorClassicCardBuilder(source.cardID, source, text, false) :
        new UnnamedCardBuilder(source.cardID, source, text, false))
        .SetOnUse(onUse);
        effects.add(builder);
        return builder;
    }

    public DynamicCardBuilder AddEffect(GenericEffect effect)
    {
        final DynamicCardBuilder builder = ((source instanceof AnimatorCard) ?
        new AnimatorCardBuilder(source.cardID, source, effect.GetText(), false) : (source instanceof AnimatorClassicCard) ?
        new AnimatorClassicCardBuilder(source.cardID, source, effect.GetText(), false) :
        new UnnamedCardBuilder(source.cardID, source, effect.GetText(), false))
        .CanUse(effect::CanUse).SetOnUse(effect::Use);
        if (effect.id != null)
        {
            builder.SetID(effect.id);
        }
        effect.OnCreateBuilder(builder);
        effects.add(builder);
        return builder;
    }

    public SelectFromPile Select(int amount, AbstractMonster m)
    {
        return Select(GameActions.Bottom, Build(false, m), amount, m);
    }

    public SelectFromPile Select(GameActions gameActions, int amount, AbstractMonster m)
    {
        return Select(gameActions, Build(false, m), amount, m);
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

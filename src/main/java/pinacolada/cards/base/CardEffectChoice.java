package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT3;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class CardEffectChoice
{
    protected final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final ArrayList<PCLCardBuilder> effects = new ArrayList<>();
    protected PCLCard source;

    public void Initialize(PCLCard source)
    {
        Initialize(source, false);
    }

    public void Initialize(PCLCard source, boolean clearEffects)
    {
        this.source = source;

        if (clearEffects)
        {
            effects.clear();
            group.clear();
        }
    }

    public boolean TryInitialize(PCLCard source)
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

            for (PCLCardBuilder b : effects)
            {
                group.addToTop(b.Build());
            }
        }

        return group;
    }

    public PCLCardBuilder AddEffect(String text, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUse)
    {
        PCLCardBuilder builder = new PCLCardBuilder(source, text, false).SetOnUse(onUse);
        effects.add(builder);
        return builder;
    }

    public PCLCardBuilder AddEffect(GenericEffect effect)
    {
        PCLCardBuilder builder = new PCLCardBuilder(source, effect.GetText(), false).SetOnUse(effect::Use);
        if (effect.id != null)
        {
            builder.SetID(effect.id);
        }
        effects.add(builder);
        return builder;
    }

    public SelectFromPile Select(int amount, AbstractMonster m)
    {
        return Select(PCLActions.Bottom, Build(false), amount, m);
    }

    public SelectFromPile Select(PCLActions gameActions, int amount, AbstractMonster m)
    {
        return Select(gameActions, Build(false), amount, m);
    }

    public SelectFromPile Select(PCLActions gameActions, CardGroup group, int amount, AbstractMonster m)
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

    public int Size() {
        return effects.size();
    }
}

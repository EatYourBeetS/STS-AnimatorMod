package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.card.RenderCardEffect;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class PlayCardFromPile extends EYBAction
{
    private final CardGroup group;
    private final boolean exhaust;
    private final boolean purge;

    public PlayCardFromPile(AbstractCard card, CardGroup group, boolean exhausts, boolean purge, AbstractMonster target)
    {
        super(ActionType.WAIT, Settings.ACTION_DUR_FAST);

        this.card = card;
        this.group = group;
        this.exhaust = exhausts;
        this.purge = purge;

        Initialize(player, target, 1);
    }

    public PlayCardFromPile(AbstractCard card, CardGroup group, boolean exhausts, boolean purge)
    {
        this(card, group, exhausts, purge, null);
    }

    @Override
    protected void FirstUpdate()
    {
        if (group.size() == 0 || !group.contains(card))
        {
            Complete();
        }
        else
        {
            group.removeCard(card);

            if (target == null)
            {
                target = GameUtilities.GetRandomEnemy(true);
            }

            GameUtilities.PlayCard(card, (AbstractMonster) target, purge, exhaust);
        }
    }
}
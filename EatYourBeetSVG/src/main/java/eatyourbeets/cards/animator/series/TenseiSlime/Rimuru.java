package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.animator.RimuruAction;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Rimuru extends AnimatorCard implements OnAfterCardPlayedSubscriber
{
    public static final EYBCardData DATA = Register(Rimuru.class)
            .SetSkill(-2, CardRarity.RARE, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Slimed(), false));

    public AbstractCard copy;

    public Rimuru()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(2, 0, 0);
        SetVolatile(true);

        this.copy = this;
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card != copy && !(card instanceof Rimuru) && !card.purgeOnUse && !card.isInAutoplay)
        {
            GameActions.Top.Add(new RimuruAction(this, card));
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAfterCardPlayed.Subscribe(this);

        if (startOfBattle)
        {
            GameActions.Top.MakeCard(new Status_Slimed(), upgraded ? player.discardPile : player.drawPile).SetEffectDuration(0.75f);
            GameEffects.List.ShowCopy(this);
        }
    }
}
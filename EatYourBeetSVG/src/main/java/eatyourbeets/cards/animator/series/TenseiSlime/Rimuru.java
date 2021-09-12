package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.RimuruAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Rimuru extends AnimatorCard implements OnAfterCardPlayedSubscriber
{
    public static final EYBCardData DATA = Register(Rimuru.class)
            .SetSkill(-2, CardRarity.RARE, EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

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
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAfterCardPlayed.Subscribe(this);
    }

    //@Formatter: Off
    @Override public final boolean canUpgrade() { return false; }
    @Override public final void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) { }
    @Override public final void upgrade() { }
    //@Formatter: On

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card != copy && !(card instanceof Rimuru) && !card.purgeOnUse && !card.isInAutoplay)
        {
            GameActions.Top.Add(new RimuruAction(this, card));
        }
    }
}
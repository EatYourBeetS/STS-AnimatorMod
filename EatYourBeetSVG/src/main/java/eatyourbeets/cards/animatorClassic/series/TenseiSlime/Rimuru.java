package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animatorClassic.RimuruAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Rimuru extends AnimatorClassicCard implements OnAfterCardPlayedSubscriber
{
    public static final EYBCardData DATA = Register(Rimuru.class).SetSeriesFromClassPackage().SetSkill(-2, CardRarity.RARE, EYBCardTarget.ALL).SetMaxCopies(2);

    public AbstractCard copy;

    public Rimuru()
    {
        super(DATA);

        Initialize(0, 0);
        
        SetShapeshifter();

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
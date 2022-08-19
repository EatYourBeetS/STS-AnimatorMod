package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class MumenRider extends AnimatorClassicCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(MumenRider.class).SetAttack(0, CardRarity.COMMON);

    private int turns;

    public MumenRider()
    {
        super(DATA);

        Initialize(3, 0, 20);

        SetExhaust(true);
        SetSeries(CardSeries.OnePunchMan);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        turns = rng.random(0, 5);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (player.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(false, false);
                CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
            else
            {
                turns -= 1;
            }
        }
        else
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}
package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;

public class MumenRider extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = Register(MumenRider.class);

    private int turns;

    public MumenRider()
    {
        super(ID, 0, CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(3, 0, 20);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        turns = AbstractDungeon.cardRandomRng.random(0, 5);
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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
        AbstractPlayer p = AbstractDungeon.player;
        if (p.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                GameActions.Bottom.MoveCard(this, p.exhaustPile, p.drawPile)
                .ShowEffect(false, false);
                PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
            }
            else
            {
                turns -= 1;
            }
        }
        else
        {
            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}
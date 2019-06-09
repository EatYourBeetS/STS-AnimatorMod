package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

public class MumenRider extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(MumenRider.class.getSimpleName());

    private int turns;

    public MumenRider()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(2, 0);

        this.exhaust = true;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SMASH);

        if (upgraded)
        {
            GameActionsHelper.DrawCard(p, 1);
        }

        turns = AbstractDungeon.miscRng.random(0, 5);
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.exhaustPile.contains(this))
        {
            if (turns <= 0)
            {
                GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand, p.exhaustPile, true));
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
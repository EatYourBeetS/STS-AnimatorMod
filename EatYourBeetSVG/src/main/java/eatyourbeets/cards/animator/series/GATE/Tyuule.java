package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Tyuule extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = Register(Tyuule.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Tyuule()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            AbstractPower poison = m.getPower(PoisonPower.POWER_ID);
            if (poison != null)
            {
                GameActions.Bottom.Add(new PoisonLoseHpAction(m, AbstractDungeon.player, poison.amount, AbstractGameAction.AttackEffect.POISON));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe((Tyuule)makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(this);

        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyPoison(p, enemy, this.magicNumber);
            GameActions.Bottom.ApplyVulnerable(p, enemy, 1);
        }

        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}
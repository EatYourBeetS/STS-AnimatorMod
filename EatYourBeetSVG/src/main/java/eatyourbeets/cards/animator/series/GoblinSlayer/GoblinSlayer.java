package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCards;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.animator.ShuffleRandomGoblinAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameUtilities;

public class GoblinSlayer extends AnimatorCard
{
    public static final String ID = Register(GoblinSlayer.class.getSimpleName(), EYBCardBadge.Special);

    public GoblinSlayer()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(7,0);

        SetRetain(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        int turnCount = PlayerStatistics.getTurnCount();
        if (turnCount % 2 == 1)
        {
            int goblins = 1;
            if (turnCount > 3)
            {
                goblins += 1;
            }
            if (turnCount > 7)
            {
                goblins += 1;
            }

            GameActions.Bottom.Add(new ShuffleRandomGoblinAction(goblins));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        SetRetain(true);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (AbstractDungeon.player.exhaustPile.size() * 3));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Top.Add(new MoveCards(p.exhaustPile, p.discardPile)
                .SetFilter(GameUtilities::IsCurseOrStatus));
        GameActions.Top.Add(new MoveCards(p.exhaustPile, p.hand)
                .SetFilter(GameUtilities::IsCurseOrStatus));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }
}
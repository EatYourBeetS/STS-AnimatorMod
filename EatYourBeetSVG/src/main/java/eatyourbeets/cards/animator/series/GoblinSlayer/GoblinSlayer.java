package eatyourbeets.cards.animator.series.GoblinSlayer;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GoblinSlayer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinSlayer.class)
            .SetAttack(1, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public GoblinSlayer()
    {
        super(DATA);

        Initialize(4, 4);
        SetUpgrade(3, 3);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetRetain(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        int turnCount = CombatStats.TurnCount(true);
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

            GameActions.Bottom.Add(new CreateRandomGoblins(goblins));
        }
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (player.exhaustPile.size() * 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);

        GameActions.Bottom.MoveCards(p.hand, p.exhaustPile)
        .SetFilter(GameUtilities::IsHindrance)
        .ShowEffect(true, true, 0.25f);

        GameActions.Bottom.MoveCards(p.discardPile, p.exhaustPile)
        .SetFilter(GameUtilities::IsHindrance)
        .ShowEffect(true, true, 0.12f);
    }
}
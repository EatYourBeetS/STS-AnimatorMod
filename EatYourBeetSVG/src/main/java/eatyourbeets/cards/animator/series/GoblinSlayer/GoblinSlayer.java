package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GoblinSlayer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinSlayer.class).SetAttack(1, CardRarity.RARE);

    public GoblinSlayer()
    {
        super(DATA);

        Initialize(4, 4);
        SetUpgrade(3, 3);
        SetScaling(1, 0, 1);

        SetRetain(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        int turnCount = CombatStats.TurnCount();
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
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        GameActions.Bottom.MoveCards(p.hand, p.exhaustPile)
        .SetFilter(GameUtilities::IsCurseOrStatus)
        .ShowEffect(true, true, 0.25f);

        GameActions.Bottom.MoveCards(p.discardPile, p.exhaustPile)
        .SetFilter(GameUtilities::IsCurseOrStatus)
        .ShowEffect(true, true, 0.12f);
    }
}
package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class GoblinSlayer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinSlayer.class)
            .SetAttack(1, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public GoblinSlayer()
    {
        super(DATA);

        Initialize(3, 4, 0, 3);
        SetUpgrade(2, 3, 0, 0);

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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);

        GameActions.Bottom.MoveCards(p.hand, p.exhaustPile)
        .SetFilter(GameUtilities::IsHindrance)
        .ShowEffect(true, true, 0.25f)
        .AddCallback(this::IncreaseDamage);

        GameActions.Bottom.MoveCards(p.discardPile, p.exhaustPile)
        .SetFilter(GameUtilities::IsHindrance)
        .ShowEffect(true, true, 0.12f)
        .AddCallback(this::IncreaseDamage);
    }

    protected void IncreaseDamage(ArrayList<AbstractCard> cards)
    {
        GameUtilities.IncreaseDamage(this, cards.size() * secondaryValue, false);
    }
}
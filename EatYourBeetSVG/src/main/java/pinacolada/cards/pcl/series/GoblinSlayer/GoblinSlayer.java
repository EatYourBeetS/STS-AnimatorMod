package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.actions.special.CreateRandomGoblins;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GoblinSlayer extends PCLCard
{
    public static final PCLCardData DATA = Register(GoblinSlayer.class)
            .SetAttack(1, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public GoblinSlayer()
    {
        super(DATA);

        Initialize(4, 4, 3);
        SetUpgrade(3, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(0,0,1);

        SetRetain(true);
        SetProtagonist(true);
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

            PCLActions.Bottom.Add(new CreateRandomGoblins(goblins));
        }
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (player.exhaustPile.size() * magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);

        PCLActions.Bottom.MoveCards(p.hand, p.exhaustPile)
        .SetFilter(PCLGameUtilities::IsHindrance)
        .ShowEffect(true, true, 0.25f);

        PCLActions.Bottom.MoveCards(p.discardPile, p.exhaustPile)
        .SetFilter(PCLGameUtilities::IsHindrance)
        .ShowEffect(true, true, 0.12f);
    }
}
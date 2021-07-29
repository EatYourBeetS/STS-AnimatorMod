package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TupleT3;

public class YaoHaDucy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YaoHaDucy.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private TupleT3<AbstractCard, Boolean, Integer> synergyCheckCache = new TupleT3<>(null, false, 0);

    public YaoHaDucy()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Green(1);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        final boolean canSynergize = other.freeToPlay() || other.costForTurn == 0 || super.HasDirectSynergy(other);
        final AbstractCard last = CombatStats.Affinities.GetLastCardPlayed();
        if (last == null)
        {
            synergyCheckCache.Clear();
        }
        else if (other == last)
        {
            final int timesPlayed = GameUtilities.GetTimesPlayedThisTurn(last);
            if (synergyCheckCache.V1 != other || synergyCheckCache.V2 == null || synergyCheckCache.V3 != timesPlayed)
            {
                synergyCheckCache.V1 = other;
                synergyCheckCache.V2 = canSynergize;
                synergyCheckCache.V3 = timesPlayed;
            }
            else
            {
                return synergyCheckCache.V2; // This allows the synergy to work with Motivate
            }
        }

        return canSynergize;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }
    }
}
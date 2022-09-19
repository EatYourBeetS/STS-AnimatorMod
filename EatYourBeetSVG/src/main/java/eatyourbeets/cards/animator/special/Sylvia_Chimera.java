package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Konosuba.Sylvia;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Sylvia_Chimera extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sylvia_Chimera.class)
            .SetAttack(2, CardRarity.SPECIAL)
            .SetSeries(Sylvia.DATA.Series)
            .SetMaxCopies(1);

    public Sylvia_Chimera()
    {
        super(DATA);

        Initialize(12, 6, 4, 2);
        SetUpgrade(4, 2, 0, 0);

        SetAffinity_Star(2, 0, 3);

        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.ApplyPoison(player, m, magicNumber);
        GameActions.Bottom.GainPlatedArmor(secondaryValue);

        GameActions.Bottom.SealAffinities(p.drawPile, p.drawPile.size(), false);
        GameActions.Bottom.SealAffinities(p.discardPile, p.discardPile.size(), false);
        GameActions.Bottom.Callback(() -> CombatStats.Affinities.UseAffinity(Affinity.Star, Integer.MAX_VALUE));
    }
}
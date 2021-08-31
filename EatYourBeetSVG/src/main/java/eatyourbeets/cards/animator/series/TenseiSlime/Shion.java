package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shion extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shion.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Shion()
    {
        super(DATA);

        Initialize(15, 0, 2, 6);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 1, 2);
        SetAffinity_Orange(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetFilter(c -> GameUtilities.HasRedAffinity(c) || GameUtilities.HasLightAffinity(c))
        .SetOptions(false, true, false)
        .AddCallback(cards ->
        {
            if (cards.size() >= magicNumber)
            {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        });

        if (isSynergizing && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}
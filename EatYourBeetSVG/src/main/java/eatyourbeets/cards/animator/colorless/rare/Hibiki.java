package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hibiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hibiki.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Kancolle);

    public Hibiki()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Star(0, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Silver(1);

        SetHitCount(3);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.GUNSHOT).forEach(d -> d
                .SetOptions(true, false));

        GameActions.Bottom.ModifyAllInstances(uuid, c -> GameUtilities.IncreaseHitCount((EYBCard) c, secondaryValue, false));

        if (info.IsSynergizing) {
            GameActions.Bottom.AddAffinity(Affinity.Star, magicNumber);
        }
    }
}
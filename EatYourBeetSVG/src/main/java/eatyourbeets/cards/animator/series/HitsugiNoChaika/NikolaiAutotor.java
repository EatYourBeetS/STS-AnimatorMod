package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NikolaiAutotor extends AnimatorCard {
    public static final EYBCardData DATA = Register(NikolaiAutotor.class)
            .SetAttack(3, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public NikolaiAutotor() {
        super(DATA);

        Initialize(16, 0, 0);
        SetUpgrade(6, 0, 0);

        SetAffinity_Fire(2);
        SetAffinity_Steel(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                int energy = Math.max(this.costForTurn, cards.size());
                GameActions.Bottom.GainEnergy(energy);
            }
        }).SetFilter(card -> !GameUtilities.IsHindrance(card));
    }
}
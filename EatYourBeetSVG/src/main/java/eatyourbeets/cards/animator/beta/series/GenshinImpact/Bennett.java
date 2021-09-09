package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Bennett extends AnimatorCard {
    public static final EYBCardData DATA = Register(Bennett.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    public Bennett() {
        super(DATA);

        Initialize(9, 0, 5, 4);
        SetUpgrade(2, 0, 3);
        SetAffinity_Red(1, 0 ,0);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        if (GameUtilities.GetHealthPercentage(player) < 0.3f || isSynergizing) {
            GameActions.Bottom.StackPower(new VigorPower(player, magicNumber));
        }
        if (!CheckAffinity(Affinity.Red)) {
            GameActions.Bottom.TakeDamage(secondaryValue, AttackEffects.SMASH);
        }
    }
}
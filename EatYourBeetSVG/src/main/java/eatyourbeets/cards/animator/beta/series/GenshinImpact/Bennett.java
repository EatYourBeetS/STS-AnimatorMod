package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Bennett extends AnimatorCard {
    public static final EYBCardData DATA = Register(Bennett.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    public Bennett() {
        super(DATA);

        Initialize(10, 0, 5, 4);
        SetUpgrade(2, 0, 2);
        SetAffinity_Fire(1, 0 ,0);

        SetAffinityRequirement(Affinity.Fire, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        if (GameUtilities.GetHealthPercentage(player) < 0.3f || info.IsSynergizing) {
            GameActions.Bottom.StackPower(new VigorPower(player, magicNumber));
        }
        if (!CheckAffinity(Affinity.Fire)) {
            GameActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.SMASH);
        }
    }
}
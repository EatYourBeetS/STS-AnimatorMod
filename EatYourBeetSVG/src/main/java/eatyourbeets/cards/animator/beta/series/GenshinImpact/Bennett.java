package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
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

        Initialize(8, 0, 3, 3);
        SetUpgrade(2, 0, 2);
        SetAffinity_Red(1, 0 ,0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        if (GameUtilities.GetHealthPercentage(player) < 0.4f) {
            GameActions.Bottom.StackPower(new VigorPower(player, magicNumber));
        }
        else {
            GameActions.Bottom.TakeDamage(secondaryValue, AttackEffects.SMASH);
        }
    }
}
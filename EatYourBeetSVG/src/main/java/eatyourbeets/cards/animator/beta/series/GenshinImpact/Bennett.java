package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Bennett extends AnimatorCard {
    public static final EYBCardData DATA = Register(Bennett.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();
    public static final int SELF_DAMAGE = 4;

    public Bennett() {
        super(DATA);

        Initialize(10, 0, 4, 2);
        SetUpgrade(4, 0, 0);
        SetAffinity_Red(1, 0 ,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.StackPower(new VigorPower(player, magicNumber));
        if (GameUtilities.GetHealthPercentage(player) < 0.2f) {
            GameActions.Bottom.StackPower(new VigorPower(player, secondaryValue));
        }
        if (info.IsSynergizing) {
            GameActions.Bottom.StackPower(new VigorPower(player, secondaryValue));
        }
        GameActions.Bottom.DealDamageAtEndOfTurn(player, player, magicNumber, AttackEffects.BLUNT_HEAVY);
    }
}
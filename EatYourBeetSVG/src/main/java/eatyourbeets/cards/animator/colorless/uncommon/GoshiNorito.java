package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class GoshiNorito extends AnimatorCard {
    public static final EYBCardData DATA = Register(GoshiNorito.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeriesFromClassPackage();

    public GoshiNorito() {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);

        SetAffinity_Air(2);
        SetAffinity_Mind();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        for (int i=0; i<p.exhaustPile.size(); i++) {

            if (rng.randomBoolean(0.5f)) {
                GameActions.Bottom.DealDamageToRandomEnemy(magicNumber, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            }
            else {
                GameActions.Bottom.GainBlock(magicNumber);
            }
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
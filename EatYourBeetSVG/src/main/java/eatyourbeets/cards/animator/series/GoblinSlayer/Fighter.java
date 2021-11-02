package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Fighter extends AnimatorCard {
    public static final EYBCardData DATA = Register(Fighter.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    public Fighter() {
        super(DATA);

        Initialize(7, 9, 2);
        SetUpgrade(2, 2, 0);

        SetAffinity_Fire();
        SetAffinity_Earth();
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();

        GameActions.Bottom.Add(new CreateRandomGoblins(magicNumber));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
    }
}
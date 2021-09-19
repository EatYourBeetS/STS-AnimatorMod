package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class EarthWall extends AnimatorCard {
    public static final EYBCardData DATA = Register(EarthWall.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public EarthWall() {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainPlatedArmor(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
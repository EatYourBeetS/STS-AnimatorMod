package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class ThornyVines extends AnimatorCard {
    public static final EYBCardData DATA = Register(ThornyVines.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public ThornyVines() {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 2);
        SetAffinity_Orange(1, 0, 0);
        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.GainTemporaryThorns(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
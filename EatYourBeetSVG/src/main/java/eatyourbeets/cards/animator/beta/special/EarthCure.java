package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.ListSelection;

public class EarthCure extends AnimatorCard {
    public static final EYBCardData DATA = Register(EarthCure.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public EarthCure() {
        super(DATA);

        Initialize(0, 2, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.RemoveCommonDebuffs(p, ListSelection.Last(0), magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
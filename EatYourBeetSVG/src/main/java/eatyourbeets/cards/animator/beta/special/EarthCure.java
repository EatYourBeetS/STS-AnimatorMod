package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.ListSelection;

public class EarthCure extends AnimatorCard {
    public static final EYBCardData DATA = Register(EarthCure.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public EarthCure() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 4);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainTemporaryHP(block);
        GameActions.Bottom.RemoveCommonDebuffs(p, ListSelection.Last(0), 1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
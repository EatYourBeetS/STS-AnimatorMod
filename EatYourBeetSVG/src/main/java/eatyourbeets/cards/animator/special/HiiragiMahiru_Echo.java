package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EchoPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class HiiragiMahiru_Echo extends AnimatorCard {
    public static final EYBCardData DATA = Register(HiiragiMahiru_Echo.class).SetPower(4, CardRarity.SPECIAL).SetSeriesFromClassPackage();

    public HiiragiMahiru_Echo() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0,0, 1);

        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new EchoPower(p, magicNumber));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.DevaPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class HiiragiMahiru_Deva extends AnimatorCard {
    public static final EYBCardData DATA = Register(HiiragiMahiru_Deva.class).SetPower(4, CardRarity.SPECIAL).SetSeriesFromClassPackage();

    public HiiragiMahiru_Deva() {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0,0);

        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new DevaPower(p));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
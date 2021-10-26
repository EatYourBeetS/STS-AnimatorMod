package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class HiiragiMahiru_Wraith extends AnimatorCard {
    public static final EYBCardData DATA = Register(HiiragiMahiru_Wraith.class).SetPower(4, CardRarity.SPECIAL).SetSeriesFromClassPackage();

    public HiiragiMahiru_Wraith() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0,0, 1);

        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, magicNumber));
        GameActions.Bottom.StackPower(new WraithFormPower(p, -1));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}
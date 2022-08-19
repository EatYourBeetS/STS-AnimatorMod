package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou_Asuramaru extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Yuuichirou_Asuramaru.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);

        SetExhaust(true);
        SetSeries(CardSeries.OwariNoSeraph);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new DemonFormPower(p, secondaryValue));
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.MakeCardInHand(new Wound());
        GameActions.Bottom.MakeCardInHand(new Wound());
    }
}
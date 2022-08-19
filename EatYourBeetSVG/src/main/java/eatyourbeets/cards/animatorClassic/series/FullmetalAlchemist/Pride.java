package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.PridePower;
import eatyourbeets.utilities.GameActions;

public class Pride extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Pride.class).SetSkill(2, CardRarity.UNCOMMON);

    public Pride()
    {
        super(DATA);

        Initialize(0,0, 1, 3);
        SetUpgrade(0, 0, 1);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
        SetSeries(CardSeries.FullmetalAlchemist);
        SetShapeshifter();
    }

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Dark::new, magicNumber);
        GameActions.Bottom.ApplyConstricted(p, m, this.secondaryValue);
        GameActions.Bottom.ApplyPower(new PridePower(p));
    }
}
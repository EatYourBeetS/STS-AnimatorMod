package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.ArcherPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Archer extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Archer.class).SetSeriesFromClassPackage().SetPower(1, CardRarity.UNCOMMON);

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 2, 0);

        
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new ArcherPower(p, magicNumber));

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }
    }
}
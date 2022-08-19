package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.WinryRockbellPower;
import eatyourbeets.utilities.GameActions;

public class WinryRockbell extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(WinryRockbell.class).SetPower(1, CardRarity.UNCOMMON);

    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, WinryRockbellPower.BLOCK_AMOUNT);
        SetUpgrade(0, 2, 0);

        SetSeries(CardSeries.FullmetalAlchemist);
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
        GameActions.Bottom.StackPower(new WinryRockbellPower(p, 1));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing)
        {
            GameActions.Bottom.UpgradeFromHand(name, 1, false);
        }
    }
}
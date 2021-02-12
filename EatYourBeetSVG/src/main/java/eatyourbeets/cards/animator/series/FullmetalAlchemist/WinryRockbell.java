package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.WinryRockbellPower;
import eatyourbeets.utilities.GameActions;

public class WinryRockbell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(WinryRockbell.class).SetPower(1, CardRarity.UNCOMMON);

    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, WinryRockbellPower.BLOCK_AMOUNT);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new WinryRockbellPower(p, 1));

        if (HasSynergy())
        {
            GameActions.Bottom.UpgradeFromHand(name, 1, false);
        }
    }
}
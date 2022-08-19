package eatyourbeets.cards.animatorClassic.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.GuildGirlPower;
import eatyourbeets.utilities.GameActions;

public class GuildGirl extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(GuildGirl.class).SetPower(1, CardRarity.UNCOMMON);

    public GuildGirl()
    {
        super(DATA);

        Initialize(0, 0, GuildGirlPower.GOLD_GAIN);
        SetUpgrade(0, 2);

        SetSeries(CardSeries.GoblinSlayer);
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
        GameActions.Bottom.StackPower(new GuildGirlPower(p, 1));
    }
}
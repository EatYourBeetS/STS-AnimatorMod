package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.GuildGirlPower;
import eatyourbeets.utilities.GameActions;

public class GuildGirl extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GuildGirl.class).SetPower(1, CardRarity.UNCOMMON);

    public GuildGirl()
    {
        super(DATA);

        Initialize(0, 0, GuildGirlPower.GOLD_GAIN);
        SetUpgrade(0, 2);

        SetSynergy(Synergies.GoblinSlayer);
        SetAffinity(0, 0, 1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new GuildGirlPower(p, 1));
    }
}
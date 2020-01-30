package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.GuildGirlPower;

public class GuildGirl extends AnimatorCard
{
    public static final String ID = Register(GuildGirl.class);

    public GuildGirl()
    {
        super(ID, 1, CardRarity.COMMON, CardType.POWER, CardTarget.SELF);

        Initialize(0,0, 4);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.StackPower(new GuildGirlPower(p, 1, magicNumber));
    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.animator.OrbCore_DarkPower;

public class OrbCore_Dark extends AnimatorCard
{
    public static final String ID = Register(OrbCore_Dark.class.getSimpleName());

    public static final int VALUE = 3;

    public OrbCore_Dark()
    {
        super(ID, 0, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActionsHelper.ChannelOrb(new Dark(), true);
        }

        GameActionsHelper.ApplyPower(p, p, new OrbCore_DarkPower(p, 1), 1);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.animator.OrbCore_ChaosPower;

public class OrbCore_Chaos extends AnimatorCard
{
    public static final String ID = Register(OrbCore_Chaos.class.getSimpleName(), EYBCardBadge.Synergy);

    public static final int VALUE = 1;

    public OrbCore_Chaos()
    {
        super(ID, 1, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE,1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
        }

        GameActionsHelper.ApplyPower(p, p, new OrbCore_ChaosPower(p, 1), 1);
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
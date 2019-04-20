package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.OrbCore_ChaosPower;

public class OrbCore_Chaos extends AnimatorCard
{
    public static final String ID = CreateFullID(OrbCore_Chaos.class.getSimpleName());

    public static final int VALUE = 3;

    public OrbCore_Chaos()
    {
        super(ID, 2, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
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
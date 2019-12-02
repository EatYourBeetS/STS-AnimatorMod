package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.orbs.Aether;
import eatyourbeets.powers.animator.OrbCore_AetherPower;
import eatyourbeets.utilities.GameActionsHelper;

public class OrbCore_Aether extends AnimatorCard
{
    public static final String ID = Register(OrbCore_Aether.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Aether()
    {
        super(ID, 1, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE,1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActionsHelper.ChannelOrb(new Aether(), true);
        }

        GameActionsHelper.ApplyPower(p, p, new OrbCore_AetherPower(p, 1), 1);
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
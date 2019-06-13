package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.animator.OrbCore_FrostPower;

public class OrbCore_Frost extends AnimatorCard
{
    public static final String ID = CreateFullID(OrbCore_Frost.class.getSimpleName());

    public static final int VALUE = 6;

    public OrbCore_Frost()
    {
        super(ID, 1, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Frost(), true);
        GameActionsHelper.ApplyPower(p, p, new OrbCore_FrostPower(p, 1), 1);
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
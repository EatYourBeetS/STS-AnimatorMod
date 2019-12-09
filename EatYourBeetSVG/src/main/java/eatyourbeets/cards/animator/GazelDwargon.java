package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.GazelDwargonPower;

public class GazelDwargon extends AnimatorCard
{
    public static final String ID = Register(GazelDwargon.class.getSimpleName(), EYBCardBadge.Synergy);

    public GazelDwargon()
    {
        super(ID, 3, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ApplyPower(p, p, new GazelDwargonPower(p));
        GameActions.Bottom.GainPlatedArmor(magicNumber);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.Motivate();
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(2);
        }
    }
}
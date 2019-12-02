package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
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
        GameActionsHelper.ApplyPower(p, p, new GazelDwargonPower(p));
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, magicNumber), magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.Motivate(1);
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
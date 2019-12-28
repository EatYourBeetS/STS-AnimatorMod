package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.GazelDwargonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GazelDwargon extends AnimatorCard
{
    public static final String ID = Register(GazelDwargon.class);

    public GazelDwargon()
    {
        super(ID, -1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseEnergyXCost(this);
        int plated = upgraded ? stacks + 1 : stacks;
        if (plated > 0)
        {
            GameActions.Bottom.GainPlatedArmor(plated);
        }
        if (stacks > 0)
        {
            GameActions.Bottom.StackPower(new GazelDwargonPower(p, stacks * magicNumber));
        }
    }
}
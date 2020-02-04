package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.powers.animator.GazelDwargonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GazelDwargon extends AnimatorCard
{
    public static final String ID = Register_Old(GazelDwargon.class);

    public GazelDwargon()
    {
        super(ID, -1, CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 4);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (upgraded)
        {
            return BlockAttribute.Instance.SetCard(this).SetText("X+1", Settings.CREAM_COLOR);
        }

        return super.GetBlockInfo();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        if (stacks > 0)
        {
            GameActions.Bottom.GainPlatedArmor(stacks);
            GameActions.Bottom.StackPower(new GazelDwargonPower(p, stacks * magicNumber));
        }

        if (upgraded)
        {
            GameActions.Bottom.GainBlock(stacks + 1);
        }
    }
}
package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.powers.animator.GazelDwargonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GazelDwargon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GazelDwargon.class).SetPower(-1, CardRarity.UNCOMMON);

    public GazelDwargon()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 1, 0);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (upgraded)
        {
            return BlockAttribute.Instance.SetCard(this).SetText("X+" + baseBlock, Settings.CREAM_COLOR);
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
            GameActions.Bottom.GainBlock(stacks + baseBlock);
        }
    }
}
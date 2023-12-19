package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.powers.animator.GazelDwargonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GazelDwargon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GazelDwargon.class)
            .SetPower(X_COST, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public GazelDwargon()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 3, 0);

        SetAffinity_Red(1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        final AbstractAttribute result = BlockAttribute.Instance.SetCard(this);
        if (baseBlock > 0)
        {
            result.mainText.SetText("X+" + result.mainText.text);
        }
        else
        {
            result.mainText.SetText("X");
        }

        return result;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final int stacks = GameUtilities.UseXCostEnergy(this);
        if (stacks > 0)
        {
            GameActions.Bottom.GainPlatedArmor(stacks);
            GameActions.Bottom.StackPower(new GazelDwargonPower(p, stacks * magicNumber));
        }

        GameActions.Bottom.GainBlock(stacks + block);
    }
}
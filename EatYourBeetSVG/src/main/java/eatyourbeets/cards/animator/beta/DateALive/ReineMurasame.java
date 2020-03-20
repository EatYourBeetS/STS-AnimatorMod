package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ReineMurasame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReineMurasame.class).SetSkill(-1, CardRarity.COMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new ShidoItsuka(), true);
    }

    public ReineMurasame()
    {
        super(DATA);

        Initialize(0, 1);
        SetUpgrade(0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return BlockAttribute.Instance.SetCard(this).SetText("X+" + baseBlock, Settings.CREAM_COLOR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);

        GameActions.Bottom.GainBlock(block + stacks);

        if (stacks > 0)
        {
            for (int i = 0; i < stacks; i++)
            {
                GameActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
                .SetUpgrade(upgraded, true);
            }

            if (HasSynergy())
            {
                GameActions.Bottom.StackPower(new EnergizedPower(p, stacks));
            }
        }
    }
}
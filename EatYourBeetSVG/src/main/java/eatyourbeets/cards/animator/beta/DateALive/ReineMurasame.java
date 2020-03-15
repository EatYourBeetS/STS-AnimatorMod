package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.*;
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

        Initialize(0, 0);
        SetUpgrade(0,5);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        if (stacks > 0)
        {
            for (int i=0; i<stacks; i++)
            {
                GameActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
                .SetUpgrade(upgraded, true)
                .AddCallback(card -> {
                    if (card != null)
                    {
                        ((EYBCard)card).SetExhaust(true);
                    }
                });
            }

            if (HasSynergy())
            {
                GameActions.Bottom.StackPower(new EnergizedPower(p, stacks));
            }
        }
    }
}
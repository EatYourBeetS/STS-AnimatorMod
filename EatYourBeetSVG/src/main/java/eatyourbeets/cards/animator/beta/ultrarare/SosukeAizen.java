package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.CounterAttackPower;
import eatyourbeets.powers.common.PhasingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SosukeAizen extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SosukeAizen.class).SetSkill(-1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public SosukeAizen()
    {
        super(DATA);

        Initialize(0, 0, 12);
        SetUpgrade(0,0,6);

        SetMultiDamage(true);
        SetExhaust(true);
        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        int energy = GameUtilities.UseXCostEnergy(this);

        if (energy > 0)
        {
            GameActions.Bottom.StackPower(new PhasingPower(p, energy));
        }

        GameActions.Bottom.StackPower(new CounterAttackPower(p, magicNumber));

        CounterAttackPower.setRemovePower(false);
    }
}
package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class SuikaIbuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuikaIbuki.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self);

    public SuikaIbuki()
    {
        super(DATA);

        Initialize(0, 15, 1, 1);
        SetUpgrade(0, 3, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(secondaryValue, upgraded);
        GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, magicNumber));
    }
}


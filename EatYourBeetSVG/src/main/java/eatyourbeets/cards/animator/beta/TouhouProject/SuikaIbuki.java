package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.TemporaryDrawReductionPower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class SuikaIbuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuikaIbuki.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self);

    public SuikaIbuki()
    {
        super(DATA);

        Initialize(0, 12, 1, 0);
        SetUpgrade(0, 16, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, magicNumber));
    }
}


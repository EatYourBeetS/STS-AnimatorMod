package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.TemporaryDrawReductionPower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SuikaIbuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuikaIbuki.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal);

    public SuikaIbuki()
    {
        super(DATA);

        Initialize(9, 6, 1, 1);
        SetUpgrade(2, 1, 0, 0);
        SetScaling(0, 0, 0);
        SetEthereal(true);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (!GameUtilities.InStance(ForceStance.STANCE_ID))
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
            GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, magicNumber));
        }

        if (HasSynergy())
        {
            GameActions.Bottom.GainForce(secondaryValue, upgraded);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.GainForce(secondaryValue, upgraded);
    }
}


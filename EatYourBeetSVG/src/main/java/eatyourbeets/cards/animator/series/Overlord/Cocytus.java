package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cocytus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cocytus.class).SetAttack(1, CardRarity.COMMON);

    public Cocytus()
    {
        super(DATA);

        Initialize(5, 4);
        SetUpgrade(3, 0);
        SetScaling(0, 0, 2);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return GameUtilities.InStance(ForceStance.STANCE_ID) ? super.GetBlockInfo() : null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (IsStarter())
        {
            GameActions.Bottom.GainForce(1, true);
        }
    }
}
package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Naotsugu extends AnimatorCard {
    public static final EYBCardData DATA = Register(Naotsugu.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);
    public static int basePlatedArmor;

    public Naotsugu() {
        super(DATA);

        Initialize(2, 8, 2, 1);
        SetUpgrade(0, 4, 0);
        SetScaling(0,0,2);

        basePlatedArmor = magicNumber;

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.Motivate(secondaryValue);

        GameActions.Bottom.GainPlatedArmor(magicNumber);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (GameUtilities.IsInStance(ForceStance.STANCE_ID))
        {
            magicNumber = basePlatedArmor + GameUtilities.GetPowerAmount(player, ForcePower.POWER_ID);
        }
        else
        {
            magicNumber = basePlatedArmor;
        }
    }
}
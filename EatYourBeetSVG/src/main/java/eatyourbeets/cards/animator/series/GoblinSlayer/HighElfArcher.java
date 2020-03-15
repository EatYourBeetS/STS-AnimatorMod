package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HighElfArcher extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HighElfArcher.class).SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Ranged);

    public HighElfArcher()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(1, 0, 1);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainAgility(1);
        }

        if (HasSynergy())
        {
            GameActions.Bottom.ModifyAllCombatInstances(uuid).AddCallback(c -> ((EYBCard)c).haste = true);
        }
    }
}
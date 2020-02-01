package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Benimaru extends AnimatorCard
{
    public static final String ID = Register(Benimaru.class);

    public Benimaru()
    {
        super(ID, 1, CardRarity.COMMON, EYBAttackType.Elemental, true);

        Initialize(3, 0, 2);
        SetUpgrade(3, 0, 0);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Fire(), true);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            if (m1.hasPower(BurningPower.POWER_ID))
            {
                GameActions.Bottom.GainTemporaryHP(magicNumber);
            }
        }
    }
}
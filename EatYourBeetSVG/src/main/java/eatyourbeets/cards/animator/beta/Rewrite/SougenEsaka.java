package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public SougenEsaka()
    {
        super(DATA);

        Initialize(6, 0, 1);
        SetUpgrade(3, 0, 2);

        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int agility = GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID);
        if (agility > 0)
        {
            GameActions.Bottom.GainBlock(agility);
        }

        if (HasSynergy())
        {
            GameActions.Bottom.Add(new RandomCardUpgrade());
        }
    }
}
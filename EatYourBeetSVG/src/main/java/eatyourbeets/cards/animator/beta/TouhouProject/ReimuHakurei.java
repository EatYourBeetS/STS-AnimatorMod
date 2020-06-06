package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ReimuHakurei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReimuHakurei.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);

    public ReimuHakurei()
    {
        super(DATA);

        Initialize(4, 0, 1, 2);
        SetUpgrade(1, 0, 0, 1);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
       GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
       GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
       GameActions.Bottom.ApplyWeak(p, m, magicNumber);
       if (IsStarter()) {
           GameActions.Bottom.Scry(secondaryValue);
       }
    }
}


package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActions;

public class AcuraShin extends AnimatorCard
{
    public static final String ID = Register(AcuraShin.class, EYBCardBadge.Special);

    public AcuraShin()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(3,2,2);

        SetCostUpgrade(-1);
        SetPiercing(true);
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL).SetPiercing(true, true);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL).SetPiercing(true, true);
        GameActions.Bottom.StackPower(new PoisonAffinityPower(p, 1));
    }
}
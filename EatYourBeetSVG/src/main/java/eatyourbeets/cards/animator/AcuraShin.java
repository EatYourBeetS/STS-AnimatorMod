package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.PoisonAffinityPower;

public class AcuraShin extends AnimatorCard
{
    public static final String ID = Register(AcuraShin.class.getSimpleName(), EYBCardBadge.Special);

    public AcuraShin()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(2,0, 3);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, 3), 3);
        GameActionsHelper.ApplyPower(p, p, new PoisonAffinityPower(p, 1), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }
}
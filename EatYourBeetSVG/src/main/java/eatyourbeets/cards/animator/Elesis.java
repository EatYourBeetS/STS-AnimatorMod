package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.ImprovedModifyDamageAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class Elesis extends AnimatorCard
{
    public static final String ID = Register(Elesis.class.getSimpleName(), EYBCardBadge.Drawn, EYBCardBadge.Discard);

    public Elesis()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(4,0, 3, 8);

        SetExhaust(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper2.ModifyAllCombatInstances(uuid, c ->
        {
            c.baseDamage += secondaryValue;
            c.applyPowers();
        });
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper2.ModifyAllCombatInstances(uuid, c -> c.baseDamage += secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper2.GainForce(magicNumber);
        GameActionsHelper2.GainAgility(magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(6);
        }
    }
}
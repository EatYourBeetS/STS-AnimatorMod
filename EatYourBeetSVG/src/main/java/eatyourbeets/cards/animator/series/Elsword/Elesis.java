package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Elesis extends AnimatorCard
{
    public static final String ID = Register_Old(Elesis.class);

    public Elesis()
    {
        super(ID, 3, CardRarity.RARE, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(4, 0, 3, 8);
        SetUpgrade(6, 0);
        SetScaling(0, 2, 2);

        SetExhaust(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ModifyAllCombatInstances(uuid, c ->
        {
            c.baseDamage += secondaryValue;
            c.applyPowers();
        });
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseDamage += secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
    }
}
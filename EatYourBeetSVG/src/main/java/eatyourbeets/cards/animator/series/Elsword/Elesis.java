package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Elesis extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Elesis.class).SetAttack(3, CardRarity.RARE);

    public Elesis()
    {
        super(DATA);

        Initialize(3, 0, 2, 9);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(0, 1, 2);

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
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
    }
}
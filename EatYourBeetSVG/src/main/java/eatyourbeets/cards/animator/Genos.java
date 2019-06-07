package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;

public class Genos extends AnimatorCard
{
    public static final String ID = CreateFullID(Genos.class.getSimpleName());

    public Genos()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(7, 0, 3);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);

        if (HasActiveSynergy() && !p.hasPower(ConservePower.POWER_ID))
        {
            GameActionsHelper.ApplyPower(p, p, new ConservePower(p, 1));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}
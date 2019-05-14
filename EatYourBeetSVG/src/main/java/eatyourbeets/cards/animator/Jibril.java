package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.CompileDriverAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.TemporaryBiasPower;

public class Jibril extends AnimatorCard
{
    public static final String ID = CreateFullID(Jibril.class.getSimpleName());

    public Jibril()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7,0, 2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.AddToBottom(new CompileDriverAction(p, 1));

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, this.magicNumber), this.magicNumber);
            ApplyTemporaryBias(p);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }

    private void ApplyTemporaryBias(AbstractPlayer p)
    {
        for (AbstractPower power : p.powers)
        {
            if (power instanceof TemporaryBiasPower)
            {
                power.amount += this.magicNumber;
                return;
            }
        }

        p.powers.add(new TemporaryBiasPower(p, this.magicNumber));
    }
}
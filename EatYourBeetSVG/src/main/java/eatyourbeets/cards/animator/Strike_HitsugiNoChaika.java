package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.GameActionsHelper;

public class Strike_HitsugiNoChaika extends Strike
{
    public static final String ID = CreateFullID(Strike_HitsugiNoChaika.class.getSimpleName());

    public Strike_HitsugiNoChaika()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5,0,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
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
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class Strike_OnePunchMan extends Strike
{
    public static final String ID = CreateFullID(Strike_OnePunchMan.class.getSimpleName());

    public Strike_OnePunchMan()
    {
        super(ID, 1, CardTarget.SELF_AND_ENEMY);

        Initialize(6,0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        PlayerStatistics.ApplyTemporaryDexterity(p, p, magicNumber);
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
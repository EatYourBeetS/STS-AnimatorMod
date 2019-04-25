package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class Strike_OwariNoSeraph extends Strike
{
    public static final String ID = CreateFullID(Strike_OwariNoSeraph.class.getSimpleName());

    public Strike_OwariNoSeraph()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();


        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, this.magicNumber, false), this.magicNumber);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
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
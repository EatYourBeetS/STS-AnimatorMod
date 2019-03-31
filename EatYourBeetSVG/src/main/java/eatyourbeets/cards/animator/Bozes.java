package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BozesPower;
import eatyourbeets.powers.PlayerStatistics;

public class Bozes extends AnimatorCard
{
    public static final String ID = CreateFullID(Bozes.class.getSimpleName());

    public Bozes()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(8,0,3);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractMonster enemy = null;
        int lowestHealth = Integer.MAX_VALUE;
        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m1.currentHealth < lowestHealth)
            {
                enemy = m1;
                lowestHealth = m1.currentHealth;
            }
        }

        if (enemy != null)
        {
            GameActionsHelper.DamageTarget(p, enemy, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }

        GameActionsHelper.ApplyPower(p, p, new BozesPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }
}
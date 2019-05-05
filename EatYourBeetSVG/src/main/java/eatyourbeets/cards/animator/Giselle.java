package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;

public class Giselle extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Giselle.class.getSimpleName());

    public Giselle()
    {
        super(ID, 2, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(14,0, 3);

        baseSecondaryValue = secondaryValue = 80;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        if (mo != null && mo.currentHealth < secondaryValue)
        {
            tmp *= 3;
        }

        return super.calculateModifiedCardDamage(player, mo, tmp);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
            upgradeSecondaryValue(20);
        }
    }
}
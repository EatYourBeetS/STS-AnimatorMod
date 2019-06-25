package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class Benimaru extends AnimatorCard
{
    public static final String ID = CreateFullID(Benimaru.class.getSimpleName());

    public Benimaru()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL);

        Initialize(4, 0, 2);

        //AddExtendedDescription();

        this.isMultiDamage = true;

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Fire(), true);
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);

        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m1.hasPower(BurningPower.POWER_ID))
            {
                GameActionsHelper.GainTemporaryHP(p, p, magicNumber);
            }
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
}
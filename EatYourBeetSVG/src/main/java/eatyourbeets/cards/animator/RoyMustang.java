package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class RoyMustang extends AnimatorCard
{
    public static final String ID = CreateFullID(RoyMustang.class.getSimpleName());

    public RoyMustang()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(7,0, 3);

        this.isMultiDamage = true;
        //AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        //GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);

        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ChannelOrb(new Fire(), true);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new FlameBarrierPower(p, this.magicNumber), this.magicNumber);
        }

//        int burning = this.magicNumber;
//        if (HasActiveSynergy())
//        {
//            burning += 2;
//        }
//
//        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
//        for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
//        {
//            GameActionsHelper.ApplyPower(p, m1, new BurningPower(m1, p, burning), burning);
//        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeMagicNumber(2);
        }
    }
}
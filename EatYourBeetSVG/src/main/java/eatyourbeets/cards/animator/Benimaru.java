package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.BurningPower;
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
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ChannelOrb(new Fire(), true);

        if (HasActiveSynergy())
        {
            for (AbstractMonster m1: PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m1, new BurningPower(m1, p, this.magicNumber), this.magicNumber);
            }
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

//    private void OnDamage(Object state, AbstractMonster monster)
//    {
//        Integer initialBlock = Utilities.SafeCast(state, Integer.class);
//        if (initialBlock != null && monster != null)
//        {
//            if (initialBlock > 0 && monster.currentBlock <= 0)
//            {
//                GameActionsHelper.GainBlock(AbstractDungeon.player, this.block);
//            }
//        }
//    }
}
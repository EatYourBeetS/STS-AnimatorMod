package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Konayuki extends AnimatorCard// implements OnBattleStartSubscriber, OnApplyPowerSubscriber
{
    public static final String ID = CreateFullID(Konayuki.class.getSimpleName());

    public Konayuki()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF);

        Initialize(25,0, 3);

//        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
//        {
//            OnBattleStart();
//        }

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (PlayerStatistics.GetStrength(AbstractDungeon.player) >= 10)
        {
            this.target = CardTarget.ENEMY;
        }
        else
        {
            this.target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (PlayerStatistics.GetStrength(p) >= 10)
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        else
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(5);
            upgradeMagicNumber(1);
        }
    }

//    @Override
//    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
//    {
//        if (target.isPlayer && power.ID.equals(StrengthPower.POWER_ID))
//        {
//            if ((PlayerStatistics.GetStrength(target) + power.amount) >= 10)
//            {
//                this.target = CardTarget.ENEMY;
//            }
//            else
//            {
//                this.target = CardTarget.SELF;
//            }
//        }
//    }
//
//    @Override
//    public void OnBattleStart()
//    {
//        PlayerStatistics.onApplyPower.Subscribe(this);
//        if (PlayerStatistics.GetStrength(AbstractDungeon.player) >= 10)
//        {
//            this.target = CardTarget.ENEMY;
//        }
//        else
//        {
//            this.target = CardTarget.SELF;
//        }
//    }
}
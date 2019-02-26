package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.subscribers.OnBattleStartSubscriber;

public class Konayuki extends AnimatorCard implements OnBattleStartSubscriber, OnApplyPowerSubscriber
{
    public static final String ID = CreateFullID(Konayuki.class.getSimpleName());

    public Konayuki()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF);

        Initialize(20,0, 3);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (CurrentStrength() >= 10)
        {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        else
        {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
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

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (target.isPlayer && power.ID.equals(StrengthPower.POWER_ID))
        {
            if ((CurrentStrength() + power.amount) >= 10)
            {
                this.target = CardTarget.ENEMY;
            }
            else
            {
                this.target = CardTarget.SELF;
            }
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onApplyPower.Subscribe(this);
        if (CurrentStrength() >= 10)
        {
            this.target = CardTarget.ENEMY;
        }
        else
        {
            this.target = CardTarget.SELF;
        }
    }

    private int CurrentStrength()
    {
        StrengthPower strength = Utilities.SafeCast(AbstractDungeon.player.getPower(StrengthPower.POWER_ID), StrengthPower.class);

        return (strength != null ? strength.amount : 0);
    }
}
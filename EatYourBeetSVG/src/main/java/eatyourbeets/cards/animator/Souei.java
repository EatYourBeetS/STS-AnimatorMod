package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Souei extends AnimatorCard
{
    public static final String ID = CreateFullID(Souei.class.getSimpleName());

    public Souei()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 6);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.Callback(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber), this::OnCompletion1, this);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }

    private int GetIntangible()
    {
        IntangiblePlayerPower intangiblePlayerPower = (IntangiblePlayerPower) AbstractDungeon.player.getPower(IntangiblePlayerPower.POWER_ID);
        if (intangiblePlayerPower != null)
        {
            return intangiblePlayerPower.amount;
        }

        return 0;
    }

    protected void OnCompletion1(Object state, AbstractGameAction action)
    {
        int currentIntangible = GetIntangible();
        AbstractPlayer p = AbstractDungeon.player;

        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            PoisonPower poisonPower = (PoisonPower)m1.getPower(PoisonPower.POWER_ID);
            if (poisonPower != null)
            {
                PoisonLoseHpAction poisonLoseHpAction = new PoisonLoseHpAction(m1, p, poisonPower.amount, AbstractGameAction.AttackEffect.POISON);
                GameActionsHelper.Callback(poisonLoseHpAction, this::OnCompletion2, currentIntangible);
            }
        }
    }

    protected void OnCompletion2(Object state, AbstractGameAction action)
    {
        if (action.target.isDying || action.target.isDead || action.target.currentHealth <= 0)
        {
            int startingIntangible = (int)state;
            AbstractPlayer p = AbstractDungeon.player;
            IntangiblePlayerPower intangiblePlayerPower = (IntangiblePlayerPower) p.getPower(IntangiblePlayerPower.POWER_ID);
            if (intangiblePlayerPower == null || intangiblePlayerPower.amount == startingIntangible)
            {
                GameActionsHelper.AddToTop(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
            }
        }
    }
}
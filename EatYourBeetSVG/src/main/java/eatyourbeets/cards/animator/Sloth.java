package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Sloth extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(Sloth.class.getSimpleName());

    public Sloth()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(2,0, 2);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (PlayerStatistics.getTurnCount() > 1)
        {
            GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, this.magicNumber));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return 1;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
    }
}
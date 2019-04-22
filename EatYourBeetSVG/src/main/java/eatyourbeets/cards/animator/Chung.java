package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Chung extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(Chung.class.getSimpleName());

    private static final int COOLDOWN = 2;

    public Chung()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(13, 8);

        this.baseSecondaryValue = this.secondaryValue = COOLDOWN;
        //this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
        this.isMultiDamage = true;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH);
        PlayerStatistics.UsePenNib();
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeBlock(2);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return COOLDOWN;
    }
}
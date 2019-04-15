package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PenNibPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class Chung extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(Chung.class.getSimpleName());

    private static final int COOLDOWN = 2;

    public Chung()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(14, 8);

        this.baseSecondaryValue = this.secondaryValue = COOLDOWN;
        //this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
        this.isMultiDamage = true;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    protected boolean ProgressCooldown()
    {
        boolean active = super.ProgressCooldown();
        if (active)
        {
            int[] multiDamage = DamageInfo.createDamageMatrix(this.baseDamage);
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.DamageAllEnemies(p, multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH);
            if (p.hasPower(PenNibPower.POWER_ID))
            {
                GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
            }
        }

        return active;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH);
        if (p.hasPower(PenNibPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
        }
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
package eatyourbeets.cards.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class Chung extends AnimatorCard_Cooldown implements CustomSavable<Integer>
{
    public static final String ID = CreateFullID(Chung.class.getSimpleName());

    private static final int COOLDOWN = 3;

    public Chung()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(16, 7);

        this.baseSecondaryValue = this.secondaryValue = COOLDOWN;
        this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
        this.isMultiDamage = true;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        if (ProgressCooldown())
        {
            GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SMASH);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
            upgradeBlock(2);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return COOLDOWN;
    }
}
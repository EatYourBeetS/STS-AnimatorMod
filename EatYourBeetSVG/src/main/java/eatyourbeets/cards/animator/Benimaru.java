package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.OnDamageAction;
import eatyourbeets.actions.OnTargetBlockBreakAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class Benimaru extends AnimatorCard
{
    public static final String ID = CreateFullID(Benimaru.class.getSimpleName());

    public Benimaru()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL);

        Initialize(7, 3, 1);

        //AddExtendedDescription();

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            DamageAction damageAction = new DamageAction(m1, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE);
            GameActionsHelper.AddToBottom(new OnDamageAction(m1, damageAction, this::OnDamage, m1.currentBlock, true));

            if (HasActiveSynergy())
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
            upgradeDamage(2);
            upgradeBlock(1);
        }
    }

    private void OnDamage(Object state, AbstractMonster monster)
    {
        Integer initialBlock = Utilities.SafeCast(state, Integer.class);
        if (initialBlock != null && monster != null)
        {
            if (initialBlock > 0 && monster.currentBlock <= 0)
            {
                GameActionsHelper.GainBlock(AbstractDungeon.player, this.block);
            }
        }
    }
}
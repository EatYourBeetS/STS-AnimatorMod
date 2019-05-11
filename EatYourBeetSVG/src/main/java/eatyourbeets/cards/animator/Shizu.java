package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.OnDamageAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.FlamingWeaponPower;

public class Shizu extends AnimatorCard
{
    public static final String ID = CreateFullID(Shizu.class.getSimpleName());

    public Shizu()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(9, 0, 1);

        AddExtendedDescription();

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.AddToBottom(new OnDamageAction(m, damageAction, this::OnDamage, this, true));
        GameActionsHelper.ApplyPower(p, p, new FlamingWeaponPower(p, 1), 1);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    private void OnDamage(Object state, AbstractMonster monster)
    {
        if (state == this && monster != null && !monster.hasPower(RegrowPower.POWER_ID))
        {
            if ((monster.isDying || monster.currentHealth <= 0) && !monster.halfDead)
            {
                AbstractPower burning = monster.getPower(BurningPower.POWER_ID);
                if (burning != null)
                {
                    AbstractDungeon.player.heal(burning.amount, true);
                }
            }
        }
    }
}
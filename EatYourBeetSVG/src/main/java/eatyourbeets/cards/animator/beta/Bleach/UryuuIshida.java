package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UryuuIshida extends AnimatorCard
{
    public static final EYBCardData DATA = Register(UryuuIshida.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);

    public UryuuIshida()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(3, 0, 0);
        SetCooldown(2, 0, this::OnCooldownCompleted);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.StackPower(new SupportDamagePower(player, magicNumber));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Callback(card -> {
            AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
            TransferWeakVulnerable(enemy);
        });
    }

    private void TransferWeakVulnerable(AbstractMonster m)
    {
        int weakToTransfer = GameUtilities.GetPowerAmount(player, WeakPower.POWER_ID);
        int vulToTransfer = GameUtilities.GetPowerAmount(player, VulnerablePower.POWER_ID);

        if (weakToTransfer > 2)
        {
            weakToTransfer = 2;
        }
        if (vulToTransfer > 2)
        {
            vulToTransfer = 2;
        }

        for (AbstractPower power : player.powers)
        {
            if (WeakPower.POWER_ID.equals(power.ID) && weakToTransfer > 0)
            {
                GameActions.Bottom.ReducePower(power, weakToTransfer);
                GameActions.Bottom.ApplyWeak(player, m, weakToTransfer);
            }
            else if (VulnerablePower.POWER_ID.equals(power.ID) && vulToTransfer > 0)
            {
                GameActions.Bottom.ReducePower(power, vulToTransfer);
                GameActions.Bottom.ApplyVulnerable(player, m, vulToTransfer);
            }
        }
    }
}
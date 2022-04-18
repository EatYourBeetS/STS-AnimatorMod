package eatyourbeets.powers.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class LaserDefensePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LaserDefensePower.class);

    protected AbstractCard lastCardPlayed;

    public LaserDefensePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        IncreasePower(GameActions.Delayed, 1);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        final AbstractCard card = GameUtilities.GetLastCardPlayed(true);
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner == player && card != lastCardPlayed)
        {
            flash();
            lastCardPlayed = card;
            GameActions.Top.DealDamage(owner, player, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetDamageEffect(target ->
            {
                SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f);
                GameEffects.List.Add(VFX.SmallLaser(owner.hb, target.hb, Color.RED));
                return 0f;
            })
            .AddCallback(player.currentHealth, (previousHP, c) ->
            {
                if (c.currentHealth < previousHP)
                {
                    GameActions.Bottom.ApplyBurning(owner, c, 1);
                }
            });
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        RemovePower();
    }
}

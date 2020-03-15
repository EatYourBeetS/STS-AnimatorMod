package eatyourbeets.cards.animator.beta.DateALive;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class KurumiTokisaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KurumiTokisaki.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.ALL);
    public static final int ATTACK_MULTIPLIER = 2;

    public KurumiTokisaki()
    {
        super(DATA);

        Initialize(12, 12);
        SetScaling(0, 2, 0);

        SetCooldown(3, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(ATTACK_MULTIPLIER);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();

        int energy = EnergyPanel.getCurrentEnergy();

        if (energy < cost)
        {
            return;
        }

        GameActions.Bottom.Callback(__ ->
        {
            int energyOnUse = EnergyPanel.getCurrentEnergy();

            if (energyOnUse >= cost)
            {
                GameActions.Bottom.SpendEnergy(cost, false);
                GameActions.Bottom.PlayCard(this, null);
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new DieDieDieEffect());

        for (int i=0; i<ATTACK_MULTIPLIER; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
        }
        GameActions.Bottom.GainBlock(block);

        if (upgraded)
        {
            GameActions.Bottom.StackPower(new EnergizedPower(p, 3));
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.SFX("POWER_TIME_WARP", 0.05F);
        GameActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED, true));
        GameActions.Bottom.Add(new SkipEnemiesTurnAction());

        for (int i=0; i<3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
        }
    }
}
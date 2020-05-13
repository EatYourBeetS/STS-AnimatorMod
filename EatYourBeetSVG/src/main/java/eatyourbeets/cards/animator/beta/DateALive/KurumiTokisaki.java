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
import eatyourbeets.utilities.GameActions;

public class KurumiTokisaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KurumiTokisaki.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.ALL);

    public KurumiTokisaki()
    {
        super(DATA);

        Initialize(12, 12, 3);

        SetCooldown(3, -1, this::OnCooldownCompleted);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetScaling(2, 2, 2);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int energy = EnergyPanel.getCurrentEnergy();
        if (energy >= costForTurn)
        {
            GameActions.Top.PlayCard(this, player.hand, null);
            GameActions.Top.SpendEnergy(costForTurn, false);
            GameActions.Top.WaitRealtime(0.3f);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new DieDieDieEffect());
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.StackPower(new EnergizedPower(p, magicNumber));

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.SFX("POWER_TIME_WARP", 0.05F);
        GameActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED, true));
        GameActions.Bottom.Add(new SkipEnemiesTurnAction());

        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
        }
    }
}
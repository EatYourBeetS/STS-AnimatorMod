package eatyourbeets.cards.animator.beta.series.DateALive;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class KurumiTokisaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KurumiTokisaki.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KurumiTokisaki()
    {
        super(DATA);

        Initialize(12, 6, 2);
        SetUpgrade(0,0,0);
        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAutoplay(true);
        SetEthereal(true);

        SetCooldown(3, 0, this::OnCooldownCompleted);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new DieDieDieEffect());
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE);
        GameActions.Bottom.StackPower(new EnergizedPower(p, magicNumber));

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.SFX("POWER_TIME_WARP", 0.05F, 0.05F);
        GameActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED, true));
        GameActions.Bottom.Add(new SkipEnemiesTurnAction());

        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
        }
    }
}
package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class DolaSchwi extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(DolaSchwi.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    private int turns = 0;
    private int turnReduction = 0;

    public DolaSchwi()
    {
        super(DATA);

        Initialize(12, 0, 2, 2);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1, 0, 3);

        SetCooldown(1, 0, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrb(new Lightning());
        GameActions.Bottom.ApplyLockOn(p,m,magicNumber);

        cooldown.ProgressCooldownAndTrigger(m);
        DolaSchwi other = (DolaSchwi) makeStatEquivalentCopy();
        other.turns = secondaryValue;
        CombatStats.onStartOfTurnPostDraw.Subscribe(other);
        turnReduction = 0;
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        turns -= 1;
        if (turns <= 0)
        {
            applyPowers();

            GameEffects.Queue.ShowCardBriefly(this);

            if (GameUtilities.GetPowers(TargetHelper.Enemies(), LockOnPower.POWER_ID).size() > 0) {
                GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.5f, 0.6f);
                GameActions.Bottom.BorderFlash(Color.SKY);
                GameActions.Bottom.VFX(VFX.Mindblast(player.dialogX, player.dialogY), 0.1f);
                for (AbstractMonster m : GameUtilities.GetEnemies(true)) {
                    if (GameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID) > 0) {
                        this.calculateCardDamage(m);
                        GameActions.Bottom.DealDamage(this, m, AttackEffects.PSYCHOKINESIS);
                    }
                }
                GameUtilities.UsePenNib();
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        turnReduction = -1;
    }
}
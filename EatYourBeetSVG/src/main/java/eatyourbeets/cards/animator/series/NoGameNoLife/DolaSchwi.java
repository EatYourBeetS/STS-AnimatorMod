package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;

public class DolaSchwi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaSchwi.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public DolaSchwi()
    {
        super(DATA);

        Initialize(16, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (cooldown.GetCurrent() == 0)
        {
            return super.GetDamageInfo();
        }

        return null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
        GameActions.Bottom.ChannelOrb(new Lightning());

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        this.calculateCardDamage(m);

        GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.5f, 0.6f);
        GameActions.Bottom.BorderFlash(Color.SKY);
        GameActions.Bottom.SFX(SFX.ATTACK_HEAVY);
        GameActions.Bottom.VFX(VFX.Mindblast(player.dialogX, player.dialogY, player.flipHorizontal), 0.1f);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
    }
}
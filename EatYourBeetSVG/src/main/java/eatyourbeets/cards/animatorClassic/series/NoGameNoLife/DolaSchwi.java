package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class DolaSchwi extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(DolaSchwi.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);

    public DolaSchwi()
    {
        super(DATA);

        Initialize(16, 0, 2);
        SetCostUpgrade(-1);

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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
        GameActions.Bottom.ChannelOrb(new Lightning());

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        this.calculateCardDamage(m);

        GameActions.Bottom.SFX("ATTACK_MAGIC_BEAM_SHORT", 0.5f, 0.7f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.SKY));
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new MindblastEffect(player.dialogX, player.dialogY, player.flipHorizontal), 0.1f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
    }
}
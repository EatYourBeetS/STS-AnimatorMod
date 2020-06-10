package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class DolaSchwi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaSchwi.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Ranged);

    public DolaSchwi()
    {
        super(DATA);

        Initialize(12, 0, 1);
        SetUpgrade(4, 0, 1);

        SetCooldown(2, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.NoGameNoLife);
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
        GameActions.Bottom.ChannelOrb(new Lightning(), true);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        this.calculateCardDamage(m);

        GameActions.Bottom.SFX("ATTACK_MAGIC_BEAM_SHORT", 0.5f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.SKY));
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new MindblastEffect(player.dialogX, player.dialogY, player.flipHorizontal), 0.1f);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
    }
}
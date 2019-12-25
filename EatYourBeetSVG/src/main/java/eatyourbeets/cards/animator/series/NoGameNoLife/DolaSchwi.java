package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.cards.base.AnimatorCard_Cooldown;
import eatyourbeets.cards.base.Synergies;

public class DolaSchwi extends AnimatorCard_Cooldown
{
    public static final String ID = Register(DolaSchwi.class.getSimpleName(), EYBCardBadge.Synergy);

    public DolaSchwi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(12, 0, 2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        UpgradeCooldown(-1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(p, new LockOnPower(m, this.magicNumber));

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }

        if (HasActiveSynergy())
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return upgraded ? 1 : 2;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        if (m == null)
        {
            m = GameUtilities.GetRandomEnemy(true);

            if (m == null)
            {
                return;
            }
        }

        this.calculateCardDamage(m);

        GameActions.Bottom.SFX("ATTACK_MAGIC_BEAM_SHORT", 0.5F);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.SKY));
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.1F);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
    }
}
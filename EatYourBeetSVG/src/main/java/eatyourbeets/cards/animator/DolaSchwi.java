package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class DolaSchwi extends AnimatorCard_Cooldown
{
    public static final String ID = Register(DolaSchwi.class.getSimpleName(), EYBCardBadge.Synergy);

    public DolaSchwi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(12,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, m, new LockOnPower(m, this.magicNumber), this.magicNumber);

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.ChannelOrb(new Lightning(), true);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-1);
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
            m = JavaUtilities.GetRandomElement(GameUtilities.GetCurrentEnemies(true));

            if (m == null)
            {
                return;
            }
        }

        this.calculateCardDamage(m);

        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_HEAVY"));
        GameActionsHelper.AddToBottom(new VFXAction(p, new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.1F));

        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
    }
}
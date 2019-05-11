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
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class DolaSchwi extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(DolaSchwi.class.getSimpleName());

    public DolaSchwi()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(16,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Lightning(), true);
        GameActionsHelper.ApplyPower(p, m, new LockOnPower(m, this.magicNumber), this.magicNumber);

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
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
            m = Utilities.GetRandomElement(PlayerStatistics.GetCurrentEnemies(true));
        }

        this.calculateCardDamage(m);

        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_HEAVY"));
        GameActionsHelper.AddToBottom(new VFXAction(p, new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.1F));

        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;

public class Giselle extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Giselle.class.getSimpleName());

    public Giselle()
    {
        super(ID, 2, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(12,0, 6);

        baseSecondaryValue = secondaryValue = 60;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        if (mo != null && mo.currentHealth < secondaryValue)
        {
            tmp *= 3;
        }

        return super.calculateModifiedCardDamage(player, mo, tmp);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m.currentHealth < secondaryValue)
        {
            GameActionsHelper.AddToBottom(new VFXAction(p, new FlameBarrierEffect(m.hb.cX, m.hb.cY), 0.5F));
            DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY);

            GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
            GameActionsHelper.AddToBottom(damageAction);
            GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }
        else
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }

        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeSecondaryValue(20);
        }
    }
}
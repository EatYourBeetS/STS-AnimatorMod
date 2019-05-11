package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.OnDamageAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Berserker extends AnimatorCard
{
    public static final String ID = CreateFullID(Berserker.class.getSimpleName());

    public Berserker()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(26,16, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY);

            GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
            GameActionsHelper.AddToBottom(new OnDamageAction(m, damageAction, this::OnDamage, m.currentHealth, true));
            GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(6);
            upgradeBlock(4);
        }
    }

    private void OnDamage(Object state, AbstractMonster monster)
    {
        Integer initialHealth = Utilities.SafeCast(state, Integer.class);
        if (initialHealth == null || monster == null)
        {
            return;
        }

        if (initialHealth == monster.maxHealth && initialHealth > monster.currentHealth)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.GainBlock(p, this.block);
            //GameActionsHelper.ApplyPower(p, monster, new StunMonsterPower(monster, 1), 1);
        }
    }
}
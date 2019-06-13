package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.OrbCore_Lightning;

import java.util.ArrayList;

public class OrbCore_LightningPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_LightningPower.class.getSimpleName());

    public OrbCore_LightningPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Lightning.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        int[] multiDamage = DamageInfo.createDamageMatrix(value, true);

        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
        ArrayList<AbstractMonster> enemies = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(int i = 0; i < enemies.size(); ++i)
        {
            AbstractMonster enemy = enemies.get(i);
            if (!enemy.isDeadOrEscaped())
            {
                GameActionsHelper.AddToBottom(new VFXAction(new LightningEffect(enemy.drawX, enemy.drawY)));
                GameActionsHelper.AddToBottom(new DamageAction(enemy, new DamageInfo(p, multiDamage[i], DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
            }
        }
    }
}


//            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//            if (target != null)
//            {
//                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
//                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
//
//                if (Settings.FAST_MODE)
//                {
//                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.1F));
//                }
//                else
//                {
//                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.3F));
//                }
//
//                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(owner, this.amount, DamageInfo.DamageType.THORNS)));
//
//                this.flash();
//                this.amount += growth;
//                updateDescription();
//            }
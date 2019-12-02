package eatyourbeets.powers.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.OrbCore_Dark;

public class OrbCore_DarkPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_DarkPower.class.getSimpleName());

    public OrbCore_DarkPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Dark.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        if (p.hand.size() < BaseMod.MAX_HAND_SIZE)
        {
            GameActionsHelper.AddToBottom(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(null, value, false), value));
            GameActionsHelper.AddToBottom(new ApplyPowerToRandomEnemyAction(p, new WeakPower(null, value, false), value));
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
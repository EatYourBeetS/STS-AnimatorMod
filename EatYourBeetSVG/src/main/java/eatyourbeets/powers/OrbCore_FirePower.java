package eatyourbeets.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.OrbCore_Fire;

public class OrbCore_FirePower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_FirePower.class.getSimpleName());

    public OrbCore_FirePower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Fire.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        GameActionsHelper.ApplyPowerToAllEnemies(p, this::CreateBurning, value);

        GameActionsHelper.SetOrder();
    }

    protected BurningPower CreateBurning(AbstractCreature m)
    {
        return new BurningPower(m, AbstractDungeon.player, value);
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
package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.cards.AnimatorCard;

public class EvePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EvePower.class.getSimpleName());

    public int growth;

    public EvePower(AbstractCreature owner, int initialDamage, int growth)
    {
        super(owner, POWER_ID);
        this.amount = initialDamage;
        this.growth = growth;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        this.growth += stackAmount;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = Utilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new EveAction(this));
        }
    }

    private class EveAction extends AnimatorAction
    {
        private final EvePower power;

        public EveAction(EvePower power)
        {
            this.power = power;
            this.amount = power.amount;
        }

        @Override
        public void update()
        {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null)
            {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));

                if (Settings.FAST_MODE)
                {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.1F));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.3F));
                }

                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS)));

                power.flash();
                power.amount += power.growth;
                power.updateDescription();
            }

            this.isDone = true;
        }
    }
}
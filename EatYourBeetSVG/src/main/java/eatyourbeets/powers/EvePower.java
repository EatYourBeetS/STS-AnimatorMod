package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class EvePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EvePower.class.getSimpleName());

    private int stacks;

    public EvePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.stacks = amount;
        this.amount = 0;

        updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount >= 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.stacks + powerStrings.DESCRIPTIONS[1] + this.stacks + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.stacks += stackAmount;
        this.updateDescription();
    }

    //    @Override
//    public void stackPower(int stackAmount)
//    {
//        this.stacks += 1;
//        this.focusGain += stackAmount;
//    }

//    @Override
//    public void atStartOfTurn()
//    {
//        super.atStartOfTurn();
//
//        for (int i = 0; i < stacks; i++)
//        {
//            GameActionsHelper.ChannelOrb(new Lightning(), true);
//        }
//    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = Utilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            amount += 1;
            if (amount >= 3)
            {
                amount = 0;
                AbstractPlayer p = AbstractDungeon.player;
                for (int i = 0; i < stacks; i++)
                {
                    GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
                }

                GameActionsHelper.GainEnergy(stacks);
                //GameActionsHelper.DrawCard(p, stacks);

                this.flash();
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
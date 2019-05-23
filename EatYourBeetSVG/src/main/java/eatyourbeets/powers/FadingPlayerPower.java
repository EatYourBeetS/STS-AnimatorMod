package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.GameActionsHelper;

public class FadingPlayerPower extends AbstractPower
{
    public static final String POWER_ID = "FadingPlayer";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public FadingPlayerPower(AbstractCreature owner, int turns)
    {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.updateDescription();
        this.loadRegion("fading");
    }

    public void updateDescription()
    {
        if (this.amount == 1)
        {
            this.description = DESCRIPTIONS[2];
        }
        else
        {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }

    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (this.amount == 1 && !this.owner.isDying)
        {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));

            for (int i = 0; i < owner.currentHealth; i ++)
            {
                GameActionsHelper.DamageTarget(owner, owner, i, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            }
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            this.updateDescription();
        }
    }

    static
    {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Fading");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
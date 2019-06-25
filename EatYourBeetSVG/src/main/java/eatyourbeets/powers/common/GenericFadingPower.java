package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import eatyourbeets.actions.animator.KillCharacterAction;
import eatyourbeets.utilities.GameActionsHelper;

public class GenericFadingPower extends AbstractPower implements CloneablePowerInterface
{
    public static final String POWER_ID = "GenericFadingPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private final boolean isPlayer;

    public GenericFadingPower(AbstractCreature owner, int turns)
    {
        this.isPlayer = owner instanceof AbstractPlayer;
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.type = PowerType.DEBUFF;
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

        if (isPlayer)
        {
            TriggerEffect();
        }
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        if (!isPlayer)
        {
            TriggerEffect();
        }
    }

    private void TriggerEffect()
    {
        AbstractDungeon.effectsQueue.add(new PowerIconShowEffect(this));

        if (this.amount == 1 && !this.owner.isDying)
        {
            GameActionsHelper.AddToBottom(new KillCharacterAction(owner, owner));
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

    @Override
    public AbstractPower makeCopy()
    {
        return new GenericFadingPower(owner, amount);
    }
}
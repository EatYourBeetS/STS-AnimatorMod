package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.utilities.PCLActions;

public class AntiArtifactSlowPower extends AbstractPower implements CloneablePowerInterface
{
    public static final String POWER_ID = "AntiArtifactSlow";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public AntiArtifactSlowPower(AbstractCreature owner, int amount)
    {
        this.name = NAME;
        this.ID = "AntiArtifactSlow";
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("slow");
        this.type = PowerType.DEBUFF;
    }

    public void atEndOfRound()
    {
        this.amount = 0;
        this.updateDescription();
    }

    public void updateDescription()
    {
        this.description = DESCRIPTIONS[0] + FontHelper.colorString(this.owner.name, "y") + DESCRIPTIONS[1];
        if (this.amount != 0)
        {
            this.description = this.description + DESCRIPTIONS[2] + this.amount * 10 + DESCRIPTIONS[3];
        }
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        PCLActions.Bottom.StackPower(new AntiArtifactSlowPower(owner, 1))
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? damage * (1f + (float) this.amount * 0.1f) : damage;
    }

    static
    {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Slow");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AntiArtifactSlowPower(owner, amount);
    }
}
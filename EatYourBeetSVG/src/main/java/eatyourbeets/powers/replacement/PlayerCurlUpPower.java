package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.GameActions;

public class PlayerCurlUpPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "Curl Up";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private boolean triggered = false;

    public PlayerCurlUpPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = "Curl Up";
        this.owner = owner;
        this.amount = amount;
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        this.loadRegion("closeUp");
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!this.triggered && damageAmount < this.owner.currentHealth && damageAmount > 0 && info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.triggered = true;

            GameActions.Bottom.GainBlock(this.owner,this.amount);
            GameActions.Last.RemovePower(owner, owner, this);
        }

        return damageAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Curl Up");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PlayerCurlUpPower(owner, amount);
    }
}
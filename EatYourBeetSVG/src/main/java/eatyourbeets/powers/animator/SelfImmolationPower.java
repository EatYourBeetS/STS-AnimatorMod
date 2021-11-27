package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class SelfImmolationPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SelfImmolationPower.class);
    public boolean justApplied;

    public SelfImmolationPower(AbstractCreature owner, int amount) {
        this(owner, amount, false);
    }

    public SelfImmolationPower(AbstractCreature owner, int amount, boolean justApplied)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        Initialize(amount, PowerType.DEBUFF, true);
        this.justApplied = justApplied;


        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameEffects.Queue.Add(VFX.Bleed(owner.hb));
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.HEART_BEAT, 1f, 1.15f, 0.95f);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        super.onPlayCard(card,m);
        if (card.block > 0) {
            ApplyDebuff(card.block * 2);
            this.flash();
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        if (justApplied) {
            justApplied = false;
        }
        else {
            ReducePower(1);
        }

    }

    private void ApplyDebuff(int amount) {
        if (amount > 0) {
            for (AbstractCreature cr : GameUtilities.GetAllCharacters(true)) {
                GameActions.Bottom.DealDamageAtEndOfTurn(owner, cr, amount, AttackEffects.CLAW);
            }
        }
    }
}

package pinacolada.powers.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class SelfImmolationPower extends PCLPower
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

        PCLGameEffects.Queue.Add(VFX.Bleed(owner.hb));
        SFX.Play(SFX.PCL_SPRAY, 1f, 1.15f, 0.95f);
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
            ApplyDebuff(card.block * amount);
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
            for (AbstractCreature cr : PCLGameUtilities.GetAllCharacters(true)) {
                PCLActions.Bottom.DealDamageAtEndOfTurn(owner, cr, amount, AttackEffects.CLAW);
            }
        }
    }
}

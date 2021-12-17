package pinacolada.powers.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class ChlammyZellPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(ChlammyZellPower.class);

    private AbstractCard.CardType lastType;

    public ChlammyZellPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        lastType = AbstractCard.CardType.SKILL;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, lastType);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card)
    {
        super.onAfterCardPlayed(card);

        if (card.type != lastType)
        {
            lastType = card.type;

            final int[] damage = DamageInfo.createDamageMatrix(amount, true);
            PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);
            PCLActions.Bottom.Cycle(name, 1);

            updateDescription();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        RemovePower();
    }
}

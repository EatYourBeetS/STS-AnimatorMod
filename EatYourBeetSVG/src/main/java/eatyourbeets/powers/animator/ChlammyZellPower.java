package eatyourbeets.powers.animator;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZellPower extends AnimatorPower
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
            GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);
            GameActions.Bottom.Cycle(name, 1);

            stackPower(1);
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

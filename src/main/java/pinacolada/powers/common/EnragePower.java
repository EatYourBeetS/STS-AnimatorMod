package pinacolada.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class EnragePower extends PCLPower
{
    public static final int GAIN = 1;
    public static final String POWER_ID = CreateFullID(EnragePower.class);

    public EnragePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.loadRegion("anger");
        this.powerIcon = this.region48;
        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, GAIN);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        super.onPlayCard(card, m);

        if (card.type == AbstractCard.CardType.SKILL)
        {
            PCLActions.Bottom.GainMight(GAIN);
            this.flash();
        }
    }
}

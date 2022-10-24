package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OddDevice_3 extends OddDevice
{
    public static final String ID = CreateFullID(OddDevice_3.class);

    public OddDevice_3()
    {
        super(ID, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        SetEnabled(true);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (IsEnabled() && GameUtilities.IsLowCost(c))
        {
            final int index = GetEffectIndex();
            if ((index == 1 && c.type == AbstractCard.CardType.POWER)
             || (index == 2 && c.type == AbstractCard.CardType.ATTACK)
             || (index == 3 && c.type == AbstractCard.CardType.SKILL))
            {
                GameActions.Bottom.PlayCopy(c, m);
                SetEnabled(false);
                flash();
            }
        }
    }
}
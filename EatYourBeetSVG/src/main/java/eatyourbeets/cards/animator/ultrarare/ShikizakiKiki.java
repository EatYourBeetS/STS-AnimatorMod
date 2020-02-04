package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final String ID = Register_Old(ShikizakiKiki.class);

    public ShikizakiKiki()
    {
        super(ID, 2, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (GameUtilities.GetPowerAmount(AgilityPower.POWER_ID) >= secondaryValue &&
            GameUtilities.GetPowerAmount(ForcePower.POWER_ID) >= secondaryValue)
        {
            GameActions.Bottom.GainEnergy(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.FetchFromPile(name, 1, p.drawPile, p.discardPile)
        .SetFilter(c -> c.type == CardType.ATTACK)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.ModifyAllCombatInstances(cards.get(0).uuid, c ->
                {
                    c.baseDamage *= 2;
                    c.applyPowers();
                });
            }
        });
    }
}
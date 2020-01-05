package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final String ID = Register(ShikizakiKiki.class, EYBCardBadge.Drawn);

    public ShikizakiKiki()
    {
        super(ID, 2, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 0, 3);

        SetEthereal(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (GameUtilities.GetPowerAmount(AgilityPower.POWER_ID) >= secondaryValue &&
            GameUtilities.GetPowerAmount(ForcePower.POWER_ID) >= secondaryValue)
        {
            GameActions.Bottom.GainEnergy(1);
            flash();
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
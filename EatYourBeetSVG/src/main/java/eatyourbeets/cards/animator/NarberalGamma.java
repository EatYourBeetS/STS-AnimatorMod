package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.common.TemporaryElectroPower;
import eatyourbeets.utilities.GameActionsHelper;

public class NarberalGamma extends AnimatorCard// implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(NarberalGamma.class.getSimpleName());

    public NarberalGamma()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.Overlord, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Lightning(), true);

        if (upgraded)
        {
            GameActionsHelper.ApplyPower(p, p, new TemporaryElectroPower(p));
        }

        GameActionsHelper.DrawCard(p, 1);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}
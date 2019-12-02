package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.common.TemporaryElectroPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class NarberalGamma extends AnimatorCard
{
    public static final String ID = Register(NarberalGamma.class.getSimpleName(), EYBCardBadge.Special);

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
            GameActionsHelper.DrawCard(p, 1);
        }

        if (EffectHistory.TryActivateSemiLimited(this.cardID))
        {
            GameActionsHelper.ApplyPower(p, p, new TemporaryElectroPower(p));
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}
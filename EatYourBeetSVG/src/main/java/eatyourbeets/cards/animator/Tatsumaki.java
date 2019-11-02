package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Air;
import eatyourbeets.powers.PlayerStatistics;

public class Tatsumaki extends AnimatorCard
{
    public static final String ID = Register(Tatsumaki.class.getSimpleName(), EYBCardBadge.Special);

    public Tatsumaki()
    {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Air(), true);
        GameActionsHelper.GainIntellect(magicNumber);

        if (PlayerStatistics.GetUniqueOrbsCount() >= 3 && PlayerStatistics.TryActivateLimited(this.cardID))
        {
            GameActionsHelper.GainEnergy(2);
            GameActionsHelper.DrawCard(p, 2);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}
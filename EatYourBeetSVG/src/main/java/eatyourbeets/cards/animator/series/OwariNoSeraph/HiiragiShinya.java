package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiShinya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiShinya.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public HiiragiShinya()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1, 1, 0);
        SetAffinity_Blue(1);

        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyLockOn(p, m, magicNumber);
        GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                final AbstractCard card = cards.get(0);
                GameUtilities.ModifyCostForTurn(card, 1, true);
                GameUtilities.Retain(card);
                GameUtilities.RefreshHandLayout();
            }
        });

        if (TryUseAffinity(Affinity.Blue))
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
        }
    }
}
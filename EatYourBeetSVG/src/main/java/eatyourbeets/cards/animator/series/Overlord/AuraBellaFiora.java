package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class AuraBellaFiora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AuraBellaFiora.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new MareBelloFiore(), true);
            });

    public AuraBellaFiora()
    {
        super(DATA);

        Initialize(0, 4, 2, 3);
        SetUpgrade(0, 3, 0);

        SetAffinity_Air(1);
        SetAffinity_Fire(1);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (IsStarter() ? magicNumber : 0);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return MareBelloFiore.DATA.ID.equals(other.cardID) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(true, true, true).AddCallback(cards -> {
            if (cards.size() > 0) {
                GameActions.Delayed.Motivate().SetFilter(GameUtilities::IsHighCost);
            }
        });

        if (info.IsSynergizing && info.GetPreviousCardID().equals(MareBelloFiore.DATA.ID) && info.TryActivateLimited())
        {
            GameActions.Bottom.DrawNextTurn(secondaryValue);
        }
    }
}
package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LizardPriest extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 7, 0, 3);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Light(2);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (GameUtilities.HasOrb(Earth.ORB_ID) ? secondaryValue : 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainInspiration(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (info.IsSynergizing && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.GainInspiration(1);
        }
    }

//    public static class LizardPriestPower extends AnimatorPower
//    {
//        public LizardPriestPower(AbstractCreature owner, int amount)
//        {
//            super(owner, LizardPriest.DATA);
//
//            Initialize(amount);
//        }
//
//        @Override
//        public void atEndOfTurn(boolean isPlayer)
//        {
//            super.atEndOfTurn(isPlayer);
//
//            GameActions.Bottom.SelectFromHand(name, amount, false)
//            .SetOptions(true, true, true)
//            .SetMessage(RetainCardsAction.TEXT[0])
//            .SetFilter(c -> GameUtilities.CanRetain(c) && GameUtilities.HasLightAffinity(c))
//            .AddCallback(cards ->
//            {
//                for (AbstractCard c : cards)
//                {
//                    GameUtilities.Retain(c);
//                }
//            });
//            RemovePower();
//        }
//    }
}
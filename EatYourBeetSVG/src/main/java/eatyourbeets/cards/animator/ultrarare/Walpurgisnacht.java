package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Walpurgisnacht extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Walpurgisnacht.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.MadokaMagica);

    public Walpurgisnacht()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainEnergyNextTurn(magicNumber);
        GameActions.Bottom.StackPower(new WalpurgisnachtPower(p, 1));
    }

    public static class WalpurgisnachtPower extends AnimatorPower
    {
        private final RandomizedList<AbstractCard> cardPool = new RandomizedList<>();

        public WalpurgisnachtPower(AbstractPlayer owner, int amount)
        {
            super(owner, Walpurgisnacht.DATA);

            Initialize(amount);
        }

        @Override
        public void playApplyPowerSfx()
        {
            SFX.Play(SFX.ORB_DARK_EVOKE, 0.45f, 0.55f, 0.95f);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            final EYBCardAffinities a = GameUtilities.GetAffinities(usedCard);
            if (a != null && a.GetLevel(Affinity.Blue, true) <= 0 && a.GetLevel(Affinity.Dark, true) <= 0)
            {
                for (int i = 0; i < amount; i++)
                {
                    GameActions.Bottom.MakeCardInHand(GetCardPool().Retrieve(rng).makeCopy())
                    .SetUpgrade(true, false);
                }

                flash();
            }
        }

        protected RandomizedList<AbstractCard> GetCardPool()
        {
            final boolean betaSeries = GR.Animator.Dungeon.HasBetaSeries;
            final RandomizedList<AbstractCard> randomCards = new RandomizedList<>();
            for (AbstractCard c : CardLibrary.getAllCards())
            {
                if (GameUtilities.IsObtainableInCombat(c))
                {
                    final EYBCardAffinities b = GameUtilities.GetAffinities(c);
                    if (b != null && (b.GetLevel(Affinity.Blue, true) > 0 || b.GetLevel(Affinity.Dark, true) > 0))
                    {
                        if (!betaSeries && c instanceof AnimatorCard)
                        {
                            final AnimatorCard c2 = (AnimatorCard) c;
                            final AnimatorLoadout loadout = GR.Animator.Data.GetLoadout(c2.series);
                            if (loadout == null || !loadout.IsBeta)
                            {
                                cardPool.Add(c);
                            }
                        }
                        else
                        {
                            cardPool.Add(c);
                        }
                    }
                }
            }

            return cardPool;
        }
    }
}
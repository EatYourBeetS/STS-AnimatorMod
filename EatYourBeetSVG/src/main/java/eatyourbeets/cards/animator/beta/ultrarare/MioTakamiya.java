package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.series.DateALive.ShidoItsuka;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class MioTakamiya extends AnimatorCard_UltraRare //TODO
{
    public static final EYBCardData DATA = Register(MioTakamiya.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.DateALive)
            .PostInitialize(data -> data.AddPreview(new ShidoItsuka(), false));

    public MioTakamiya()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetAffinity_Light(2, 0, 0);
        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MioTakamiyaPower(p, magicNumber));
    }

    public static class MioTakamiyaPower extends AnimatorPower
    {
        private static ArrayList<AbstractCard> cardPool;
        public static final int HP_LOSS = 4;

        private static void InitializePool() {
            if (cardPool == null)
            {
                cardPool = new ArrayList<>();
                MioTakamiya fake = new MioTakamiya();

                for (AbstractCard c : GameUtilities.GetAvailableCards())
                {
                    if (c.rarity == CardRarity.COMMON || c.rarity == CardRarity.UNCOMMON || c.rarity == CardRarity.RARE)
                    {
                        if (c instanceof AnimatorCard
                                && fake.WouldSynergize(c))
                        {
                            cardPool.add(c);
                        }
                    }
                }
            }
        }

        public MioTakamiyaPower(AbstractPlayer owner, int amount)
        {
            super(owner, MioTakamiya.DATA);

            this.amount = amount;

            InitializePool();
            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, HP_LOSS);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (this.amount > 0) {
                final AnimatorCard card = JUtils.SafeCast(usedCard, AnimatorCard.class);
                if (card != null && card.HasSynergy())
                {
                    GameActions.Bottom.LoseHP(HP_LOSS, AttackEffects.SLASH_VERTICAL);

                    AnimatorCard newCard = (AnimatorCard) GameUtilities.GetRandomElement(cardPool).makeCopy();
                    GameUtilities.ModifyCostForCombat(newCard, 0, false);
                    newCard.SetAutoplay(true);
                    newCard.SetPurge(true);

                    GameActions.Bottom.MakeCardInDrawPile(newCard)
                            .SetUpgrade(true, true);
                    this.amount -= 1;
                    updateDescription();
                    flash();
                }
            }
        }
    }

}
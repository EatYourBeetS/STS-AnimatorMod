package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.card.PermanentUpgradeEffect;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Kaijin extends AnimatorCard implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(Kaijin.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Kaijin()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new KaijinPower(p, magicNumber));
    }

    @Override
    public boolean OnAddToDeck()
    {
        GameEffects.TopLevelQueue.Add(new PermanentUpgradeEffect()).SetFilter(c -> CardRarity.BASIC.equals(c.rarity));

        return true;
    }

    public static class KaijinPower extends AnimatorPower
    {
        public static final String POWER_ID = CreateFullID(KaijinPower.class);

        public KaijinPower(AbstractPlayer owner, int amount)
        {
            super(owner, Kaijin.DATA);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (isPlayer && !player.hand.isEmpty())
            {
                GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        final AbstractCard card = cards.get(0);
                        if (card.baseBlock >= 0)
                        {
                            card.baseBlock += amount;
                        }
                        if (card.baseDamage >= 0)
                        {
                            card.baseDamage += amount;
                        }

                        GameUtilities.Retain(card);
                    }
                });
            }
        }
    }
}
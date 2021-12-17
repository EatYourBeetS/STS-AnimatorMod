package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.card.PermanentUpgradeEffect;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Kaijin extends PCLCard implements OnAddToDeckListener
{
    public static final PCLCardData DATA = Register(Kaijin.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Kaijin()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new KaijinPower(p, magicNumber));
    }

    @Override
    public boolean OnAddToDeck()
    {
        PCLGameEffects.TopLevelQueue.Add(new PermanentUpgradeEffect()).SetFilter(c -> CardRarity.BASIC.equals(c.rarity));

        return true;
    }

    public static class KaijinPower extends PCLPower
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
                PCLActions.Bottom.SelectFromHand(name, 1, false)
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

                        PCLGameUtilities.Retain(card);
                    }
                });
            }
        }
    }
}
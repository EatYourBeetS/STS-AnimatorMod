package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Soujiro_Hisako;
import pinacolada.cards.pcl.special.Soujiro_Isami;
import pinacolada.cards.pcl.special.Soujiro_Kawara;
import pinacolada.cards.pcl.special.Soujiro_Nazuna;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class Soujiro extends PCLCard
{
    private static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static final PCLCardData DATA = Register(Soujiro.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                cardPool.add(new Soujiro_Isami());
                cardPool.add(new Soujiro_Kawara());
                cardPool.add(new Soujiro_Hisako());
                cardPool.add(new Soujiro_Nazuna());
                for (AbstractCard c : cardPool)
                {
                    data.AddPreview(c, true);
                }
            });

    public Soujiro()
    {
        super(DATA);

        Initialize(14, 0, 1);
        SetUpgrade(2, 0, 1);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Red(0,0,1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            cardChoices.clear();
            upgradedCardChoices.clear();
            for (AbstractCard c : cardPool)
            {
                cardChoices.group.add(c.makeCopy());
                c = c.makeCopy();
                c.upgrade();
                upgradedCardChoices.group.add(c);
            }
        }

        if (cardChoices.size() > 0)
        {
            PCLActions.Last.Callback(() ->
            {
                PCLActions.Bottom.Flash(this)
                .SetDuration(Settings.ACTION_DUR_MED, true);
                PCLActions.Bottom.SelectFromPile(name, 1, upgraded ? upgradedCardChoices : cardChoices)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        PCLActions.Bottom.MakeCardInHand(c);
                        upgradedCardChoices.removeCard(c.cardID);
                        cardChoices.removeCard(c.cardID);
                    }
                });
            });
        }
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (magicNumber * GetHandAffinity(PCLAffinity.General));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
    }
}
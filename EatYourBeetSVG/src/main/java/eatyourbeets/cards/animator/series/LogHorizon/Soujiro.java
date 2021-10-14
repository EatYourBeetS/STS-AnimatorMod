package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Soujiro_Hisako;
import eatyourbeets.cards.animator.special.Soujiro_Isami;
import eatyourbeets.cards.animator.special.Soujiro_Kawara;
import eatyourbeets.cards.animator.special.Soujiro_Nazuna;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Soujiro extends AnimatorCard
{
    private static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static final EYBCardData DATA = Register(Soujiro.class)
            .SetAttack(3, CardRarity.RARE, EYBAttackType.Normal)
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

        SetAffinity_Green(2, 0, 2);
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
            GameActions.Last.Callback(() ->
            {
                GameActions.Bottom.Flash(this)
                .SetDuration(Settings.ACTION_DUR_MED, true);
                GameActions.Bottom.SelectFromPile(name, 1, upgraded ? upgradedCardChoices : cardChoices)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        GameActions.Bottom.MakeCardInHand(c);
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
        return super.GetInitialDamage() + (magicNumber * GetHandAffinity(Affinity.General));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
    }
}
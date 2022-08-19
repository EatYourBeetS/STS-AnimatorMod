package eatyourbeets.cards.animatorClassic.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Soujiro_Hisako;
import eatyourbeets.cards.animatorClassic.special.Soujiro_Isami;
import eatyourbeets.cards.animatorClassic.special.Soujiro_Kawara;
import eatyourbeets.cards.animatorClassic.special.Soujiro_Nazuna;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Soujiro extends AnimatorClassicCard
{
    private static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static final EYBCardData DATA = Register(Soujiro.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal)
    .PostInitialize(data ->
    {
        cardPool.add(data.AddPreview(new Soujiro_Isami(), true).CreateNewInstance());
        cardPool.add(data.AddPreview(new Soujiro_Kawara(), true).CreateNewInstance());
        cardPool.add(data.AddPreview(new Soujiro_Hisako(), true).CreateNewInstance());
        cardPool.add(data.AddPreview(new Soujiro_Nazuna(), true).CreateNewInstance());
    });

    public Soujiro()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(2, 0, 1);
        SetScaling(0,1, 1);

        SetMartialArtist();
        SetSeries(CardSeries.LogHorizon);
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

//    @Override
//    protected float GetInitialDamage()
//    {
//        return super.GetInitialDamage() + (magicNumber * GameUtilities.GetTeamwork(null));
//    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}
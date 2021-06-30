package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Soujiro_Hisako;
import eatyourbeets.cards.animator.special.Soujiro_Isami;
import eatyourbeets.cards.animator.special.Soujiro_Kawara;
import eatyourbeets.cards.animator.special.Soujiro_Nazuna;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Soujiro extends AnimatorCard
{
    private static final ArrayList<AbstractCard> cardPool = new ArrayList<>();
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static final EYBCardData DATA = Register(Soujiro.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal);
    static
    {
        cardPool.add(DATA.AddPreview(new Soujiro_Isami(), true));
        cardPool.add(DATA.AddPreview(new Soujiro_Kawara(), true));
        cardPool.add(DATA.AddPreview(new Soujiro_Hisako(), true));
        cardPool.add(DATA.AddPreview(new Soujiro_Nazuna(), true));
    }

    public Soujiro()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(2, 0, 1);
        SetScaling(0,1, 1);

        SetMartialArtist();
        SetSynergy(Synergies.LogHorizon);
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
        return super.GetInitialDamage() + (magicNumber * GameUtilities.GetTeamwork(null));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}
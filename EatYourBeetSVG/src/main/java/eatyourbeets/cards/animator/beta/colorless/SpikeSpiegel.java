package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.SwordfishII;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class SpikeSpiegel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SpikeSpiegel.class).SetAttack(3, CardRarity.RARE).SetColor(CardColor.COLORLESS);

    static
    {
        DATA.AddPreview(new SwordfishII(), true);
    }

    public SpikeSpiegel()
    {
        super(DATA);

        Initialize(18, 0, 0);
        SetUpgrade(2, 0, 0);

        SetSeries(CardSeries.CowboyBebop);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return (other.rarity.equals(CardRarity.BASIC)) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c != null && c.rarity.equals(CardRarity.BASIC))
            {
                randomizedList.Add(c);
            }
        }
        AbstractCard card = randomizedList.Retrieve(rng);
        if (card != null)
        {
            GameActions.Bottom.Motivate(card, 1);
        }

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDrawPile(new SwordfishII()).SetUpgrade(upgraded, false);
        }

    }
}
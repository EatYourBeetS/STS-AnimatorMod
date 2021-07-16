package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.SwordfishII;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class SpikeSpiegel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SpikeSpiegel.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CowboyBebop);

    static
    {
        DATA.AddPreview(new SwordfishII(), true);
    }

    public SpikeSpiegel()
    {
        super(DATA);

        Initialize(6, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(2, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c != null && c.tags.contains(CardTags.STRIKE))
            {
                randomizedList.Add(c);
            }
        }

        GameActions.Bottom.Callback(() ->
        {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard card = randomizedList.Retrieve(rng);
                if (card != null)
                {
                    GameActions.Top.PlayCard(card, player.drawPile, m)
                            .SpendEnergy(false);
                }
            }
        });


        if (CheckTeamwork(AffinityType.Red, 2) && CheckTeamwork(AffinityType.Green, 2) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDrawPile(new SwordfishII()).SetUpgrade(upgraded, false);
        }

    }
}
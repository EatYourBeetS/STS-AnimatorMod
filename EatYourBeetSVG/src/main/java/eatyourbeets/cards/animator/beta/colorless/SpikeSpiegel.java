package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.SwordfishII;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardTags.STARTER_STRIKE;

public class SpikeSpiegel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SpikeSpiegel.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CowboyBebop)
            .PostInitialize(data -> data.AddPreview(new SwordfishII(), false));

    public SpikeSpiegel()
    {
        super(DATA);

        Initialize(6, 0, 3, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(2, 0, 1);

        SetAffinityRequirement(Affinity.General, 10);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);

        RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c != null && (c.tags.contains(CardTags.STRIKE) || c.tags.contains(STARTER_STRIKE)))
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


        if (CheckAffinity(Affinity.General) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                GameActions.Bottom.MakeCardInDrawPile(new SwordfishII()).SetUpgrade(upgraded, false);
            });
        }

    }
}
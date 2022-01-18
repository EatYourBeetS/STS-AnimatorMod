package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.SwordfishII;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardTags.STARTER_STRIKE;

public class SpikeSpiegel extends PCLCard
{
    public static final PCLCardData DATA = Register(SpikeSpiegel.class).SetAttack(3, CardRarity.RARE, PCLAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CowboyBebop)
            .PostInitialize(data -> data.AddPreview(new SwordfishII(), false));

    public SpikeSpiegel()
    {
        super(DATA);

        Initialize(6, 0, 3, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 10);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);

        RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
        for (AbstractCard c : player.drawPile.group)
        {
            if (c != null && (c.tags.contains(CardTags.STRIKE) || c.tags.contains(STARTER_STRIKE)))
            {
                randomizedList.Add(c);
            }
        }

        PCLActions.Bottom.Callback(() ->
        {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard card = randomizedList.Retrieve(rng);
                if (card != null)
                {
                    PCLActions.Top.PlayCard(card, player.drawPile, m)
                            .SpendEnergy(false);
                }
            }
        });


        if (CheckAffinity(PCLAffinity.General) && CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                PCLActions.Bottom.MakeCardInDrawPile(new SwordfishII()).SetUpgrade(upgraded, false);
            });
        }

    }
}
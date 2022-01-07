package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.modifiers.BlockModifiers;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import static pinacolada.resources.GR.Enums.CardTags.PCL_ETHEREAL;
import static pinacolada.resources.GR.Enums.CardTags.PCL_EXHAUST;

public class Nirvash extends PCLCard
{
    public static final PCLCardData DATA = Register(Nirvash.class)
            .SetSkill(0, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.EurekaSeven)
            .PostInitialize(data ->
            {
                data.AddPreview(new Nirvash(1), false);
                data.AddPreview(new Nirvash(2), false);
                data.AddPreview(new Nirvash(3), false);
            });
    public static final int SILVER_SCALING = 6;

    private final int effect;

    public Nirvash()
    {
        this(0);
    }

    private Nirvash(int effect)
    {
        super(DATA);

        Initialize(0, 0, 3, 3);

        if ((this.effect = effect) == 0)
        {
            SetAffinity_Silver(1);
            SetAffinity_Green(1);

            SetPurge(true);

            SetAffinityRequirement(PCLAffinity.Silver, 6);
        }
        else
        {
            cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[effect], true);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetEthereal(false);
        SetHaste(true);
    }

    @Override
    public PCLCardPreview GetCardPreview()
    {
        return effect > 0 ? null : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, 1, upgraded ? new CardGroup[]{p.hand, p.discardPile, p.drawPile} : new CardGroup[]{p.hand, p.discardPile})
        .SetOptions(false, false)
        .SetFilter(c -> !c.hasTag(GR.Enums.CardTags.VOLATILE) && c instanceof PCLCard)
        .AddCallback(cards ->
        {
           boolean isPermanent = (PCLJUtils.All(cards, c -> PCLGameUtilities.GetMasterDeckInstance(c.uuid) != null) && TrySpendAffinity(PCLAffinity.Silver) && CombatStats.TryActivateLimited(cardID));

           for (AbstractCard c : cards)
           {
               final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
               group.group.add(new Nirvash(1));
               group.group.add(new Nirvash(2));
               group.group.add(new Nirvash(3));
               PCLActions.Bottom.SelectFromPile(name, 1, group)
               .SetOptions(false, false)
               .CancellableFromPlayer(false)
               .AddCallback(c, (selected, nirvash) -> {
                   if (isPermanent) {
                       PCLActions.Bottom.ModifyAllInstances(selected.uuid, copyOfCard ->
                               ((Nirvash)nirvash.get(0)).ApplyEffect((PCLCard) copyOfCard, true))
                               .IncludeMasterDeck(true)
                               .IsCancellable(false);

                       //noinspection StatementWithEmptyBody
                       while (p.masterDeck.removeCard(cardID));

                       PCLGameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                       for (AbstractCard card : PCLGameUtilities.GetAllInBattleInstances(uuid))
                       {
                           PCLActions.Bottom.Purge(card).ShowEffect(true);
                       }
                   }
                   else {
                       ((Nirvash)nirvash.get(0)).ApplyEffect((PCLCard) selected, false);
                   }
               });
           }
        });
    }

    protected void ApplyEffect(PCLCard eCard, boolean isPermanent)
    {
        switch (effect)
        {
            case 1:
                if (eCard.costForTurn >= 0)
                {
                    int addHitCount = eCard.hitCount;
                    if (eCard.baseBlock > 0)
                    {
                        int addBlock = eCard.baseBlock;
                        BlockModifiers.For(eCard).Add(cardID, addBlock);
                        if (isPermanent) {
                            eCard.auxiliaryData.modifiedBlock += addBlock;
                        }
                    }
                    CostModifiers.For(eCard).Add(cardID, 1);
                    eCard.hitCount += addHitCount;
                    if (isPermanent) {
                        eCard.auxiliaryData.modifiedCost += 1;
                        eCard.auxiliaryData.modifiedHitCount += addHitCount;
                    }
                    eCard.flash();
                }
                break;

            case 2:
                if (eCard.isEthereal) {
                    eCard.exhaust = true;
                    if (isPermanent) {
                        eCard.auxiliaryData.modifiedTags.add(PCL_EXHAUST);
                    }
                }
                eCard.isEthereal = true;
                if (eCard.costForTurn >= 0)
                {
                    CostModifiers.For(eCard).Add(cardID, -1);
                }
                if (isPermanent) {
                    eCard.auxiliaryData.modifiedCost -= 1;
                    eCard.auxiliaryData.modifiedTags.add(PCL_ETHEREAL);
                }
                eCard.flash();
                break;

            case 3:
                PCLCardAffinities newAffinities = new PCLCardAffinities(null);
                newAffinities.Set(PCLAffinity.Silver, 2);
                newAffinities.SetScaling(PCLAffinity.Silver, SILVER_SCALING);
                if (eCard.affinities.HasStar()) {
                    newAffinities.SetStar(eCard.affinities.Star.level);
                }
                eCard.affinities.Initialize(newAffinities);
                eCard.SetInnate(true);
                if (isPermanent) {
                    eCard.auxiliaryData.modifiedAffinities = new int[]{-2,-2,-2,-2,-2,-2,2};
                    eCard.auxiliaryData.modifiedScaling = new int[]{-999,-999,-999,-999,-999,-999,SILVER_SCALING};
                    eCard.auxiliaryData.modifiedTags.add(PCL_INNATE);
                }
                eCard.flash();
                break;
        }
    }
}
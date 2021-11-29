package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import static eatyourbeets.resources.GR.Enums.CardTags.ANIMATOR_ETHEREAL;
import static eatyourbeets.resources.GR.Enums.CardTags.ANIMATOR_EXHAUST;

public class Nirvash extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nirvash.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
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
            SetAffinity_Silver(2);
            SetAffinity_Green(1);

            SetPurge(true);

            SetAffinityRequirement(Affinity.Silver, 6);
        }
        else
        {
            cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[effect], true);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
        SetHaste(true);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return effect > 0 ? null : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromPile(name, 1, upgraded ? new CardGroup[]{p.hand, p.discardPile, p.drawPile} : new CardGroup[]{p.hand, p.discardPile})
        .SetOptions(false, false)
        .SetFilter(c -> !c.hasTag(GR.Enums.CardTags.VOLATILE) && c instanceof EYBCard)
        .AddCallback(cards ->
        {
           boolean isPermanent = (JUtils.All(cards, c -> GameUtilities.GetMasterDeckInstance(c.uuid) != null) && TrySpendAffinity(Affinity.Silver) && CombatStats.TryActivateLimited(cardID));

           for (AbstractCard c : cards)
           {
               final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
               group.group.add(new Nirvash(1));
               group.group.add(new Nirvash(2));
               group.group.add(new Nirvash(3));
               GameActions.Bottom.SelectFromPile(name, 1, group)
               .SetOptions(false, false)
               .CancellableFromPlayer(false)
               .AddCallback(c, (selected, nirvash) -> {
                   if (isPermanent) {
                       GameActions.Bottom.ModifyAllInstances(selected.uuid, copyOfCard ->
                               ((Nirvash)nirvash.get(0)).ApplyEffect((EYBCard) copyOfCard, true))
                               .IncludeMasterDeck(true)
                               .IsCancellable(false);

                       //noinspection StatementWithEmptyBody
                       while (p.masterDeck.removeCard(cardID));

                       GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                       for (AbstractCard card : GameUtilities.GetAllInBattleInstances(uuid))
                       {
                           GameActions.Bottom.Purge(card).ShowEffect(true);
                       }
                   }
                   else {
                       ((Nirvash)nirvash.get(0)).ApplyEffect((EYBCard) selected, false);
                   }
               });
           }
        });
    }

    protected void ApplyEffect(EYBCard eCard, boolean isPermanent)
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
                        eCard.auxiliaryData.modifiedTags.add(ANIMATOR_EXHAUST);
                    }
                }
                eCard.isEthereal = true;
                if (eCard.costForTurn >= 0)
                {
                    CostModifiers.For(eCard).Add(cardID, -1);
                }
                if (isPermanent) {
                    eCard.auxiliaryData.modifiedCost -= 1;
                    eCard.auxiliaryData.modifiedTags.add(ANIMATOR_ETHEREAL);
                }
                eCard.flash();
                break;

            case 3:
                EYBCardAffinities newAffinities = new EYBCardAffinities(null);
                newAffinities.Set(Affinity.Silver, 2);
                newAffinities.SetScaling(Affinity.Silver, SILVER_SCALING);
                if (eCard.affinities.HasStar()) {
                    newAffinities.SetStar(eCard.affinities.Star.level);
                }
                eCard.affinities.Initialize(newAffinities);
                eCard.SetInnate(true);
                if (isPermanent) {
                    eCard.auxiliaryData.modifiedAffinities = new int[]{-2,-2,-2,-2,-2,-2,2};
                    eCard.auxiliaryData.modifiedScaling = new int[]{-999,-999,-999,-999,-999,-999,SILVER_SCALING};
                    eCard.auxiliaryData.modifiedTags.add(ANIMATOR_INNATE);
                }
                eCard.flash();
                break;
        }
    }
}
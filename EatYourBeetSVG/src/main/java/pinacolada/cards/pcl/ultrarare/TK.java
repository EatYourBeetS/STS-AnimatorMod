package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.interfaces.subscribers.OnLosingHPSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class TK extends PCLCard_UltraRare implements OnLosingHPSubscriber
{
    public static final PCLCardData DATA = Register(TK.class).SetAttack(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.AngelBeats);

    public TK()
    {
        super(DATA);

        Initialize(4, 0, 2, 3);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Light(1, 0, 2);

        SetAfterlife(true);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        this.AddScaling(PCLAffinity.Red, 2);
        this.AddScaling(PCLAffinity.Green, 2);
        this.AddScaling(PCLAffinity.Light, 2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onLosingHP.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        PCLActions.Bottom.GainInvocation(magicNumber, true);
    }

    @Override
    public int OnLosingHP(int damageAmount)
    {
        if (CombatStats.HasActivatedLimited(cardID))
        {
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            return damageAmount;
        }

        if (damageAmount > 0 && player.currentHealth <= damageAmount && CanRevive())
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.masterDeck.group);
            while (group.size() < secondaryValue && pile.Size() > 0)
            {
                group.addToTop(pile.Retrieve(rng));
            }

            PCLActions.Last.SelectFromPile(name, 1, group).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    group.removeCard(c);
                    PCLActions.Bottom.ModifyAllInstances(c.uuid, copyOfCard -> {
                        PCLCard eCard = PCLJUtils.SafeCast(copyOfCard, PCLCard.class);
                        if (eCard != null) {
                            eCard.auxiliaryData.modifiedTags.add(AFTERLIFE);
                            eCard.SetAfterlife(true);
                        }
                    })
                            .IncludeMasterDeck(true)
                            .IsCancellable(false);
                }

                for (AbstractCard c : group.group) {
                    if (PCLGameUtilities.CanRemoveFromDeck(c)) {
                        PCLGameEffects.Queue.Add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                        player.masterDeck.removeCard(c);
                        for (AbstractCard card : PCLGameUtilities.GetAllInBattleInstances(c.uuid))
                        {
                            player.discardPile.removeCard(card);
                            player.drawPile.removeCard(card);
                            player.hand.removeCard(card);
                        }

                    }
                }
            });

            CombatStats.TryActivateLimited(cardID);
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            PCLGameEffects.List.Add(new ShowCardBrieflyEffect(makeStatEquivalentCopy()));
            PCLGameUtilities.RefreshHandLayout();
            return 0;
        }

        return damageAmount;
    }

    private boolean CanRevive()
    {
        return PCLGameUtilities.InBattle() && player.exhaustPile.contains(this);
    }

}
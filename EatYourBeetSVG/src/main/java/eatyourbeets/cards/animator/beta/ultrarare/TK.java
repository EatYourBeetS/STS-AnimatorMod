package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.*;

import static eatyourbeets.resources.GR.Enums.CardTags.AFTERLIFE;

public class TK extends AnimatorCard_UltraRare implements OnLoseHpSubscriber
{
    public static final EYBCardData DATA = Register(TK.class).SetAttack(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.AngelBeats);

    public TK()
    {
        super(DATA);

        Initialize(4, 0, 2, 3);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Light(1, 0, 2);

        AfterLifeMod.Add(this);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        this.AddScaling(Affinity.Red, 2);
        this.AddScaling(Affinity.Green, 2);
        this.AddScaling(Affinity.Light, 2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onLoseHp.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        GameActions.Bottom.GainSupercharge(magicNumber, true);
    }

    @Override
    public int OnLoseHp(int damageAmount)
    {
        if (CombatStats.HasActivatedLimited(cardID))
        {
            CombatStats.onLoseHp.Unsubscribe(this);
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

            GameActions.Last.SelectFromPile(name, 1, group).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    group.removeCard(c);
                    GameActions.Bottom.ModifyAllInstances(c.uuid, copyOfCard -> {
                        EYBCard eybCard = JUtils.SafeCast(copyOfCard, EYBCard.class);
                        if (eybCard != null) {
                            eybCard.auxiliaryData.modifiedTags.add(AFTERLIFE);
                            AfterLifeMod.Add(eybCard);
                        }
                    })
                            .IncludeMasterDeck(true)
                            .IsCancellable(false);
                }

                for (AbstractCard c : group.group) {
                    if (GameUtilities.CanRemoveFromDeck(c)) {
                        GameEffects.Queue.Add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                        player.masterDeck.removeCard(c);
                        for (AbstractCard card : GameUtilities.GetAllInBattleInstances(c.uuid))
                        {
                            player.discardPile.removeCard(card);
                            player.drawPile.removeCard(card);
                            player.hand.removeCard(card);
                        }

                    }
                }
            });

            CombatStats.TryActivateLimited(cardID);
            CombatStats.onLoseHp.Unsubscribe(this);
            GameEffects.List.Add(new ShowCardBrieflyEffect(makeStatEquivalentCopy()));
            GameUtilities.RefreshHandLayout();
            return 0;
        }

        return damageAmount;
    }

    private boolean CanRevive()
    {
        return GameUtilities.InBattle() && player.exhaustPile.contains(this);
    }

}
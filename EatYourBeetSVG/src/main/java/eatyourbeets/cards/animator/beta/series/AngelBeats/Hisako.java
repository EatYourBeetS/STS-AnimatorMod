package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAfterlifeSubscriber;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Hisako extends AnimatorCard implements OnPurgeSubscriber, OnAfterlifeSubscriber
{
    public static final EYBCardData DATA = Register(Hisako.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public Hisako()
    {
        super(DATA);

        Initialize(0, 7, 3, 1);
        SetUpgrade(0,0,1,0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(Affinity.General, 3);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
        SetAffinityRequirement(Affinity.General, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback((cards) -> {
            if (cards.size() > 0) {
                Affinity af = AffinityToken.GetAffinityFromCardID(cards.get(0).cardID);

                final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.drawPile.group);
                while (group.size() < magicNumber && pile.Size() > 0)
                {
                    AbstractCard c = pile.Retrieve(rng);
                    if (c instanceof AnimatorCard && ((AnimatorCard) c).affinities.GetLevel(af) > 0)
                    group.addToTop(c);
                }

                if (group.size() >= 0)
                {
                    GameActions.Bottom.FetchFromPile(name, 1, group).SetOptions(false, true);
                }
            }
        });
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (player.hand.contains(this)) {
            GameActions.Bottom.GainSupercharge(secondaryValue);
        }
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (player.hand.contains(this)) {
            GameActions.Bottom.GainSupercharge(secondaryValue);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        CombatStats.onAfterlife.Subscribe(this);
        CombatStats.onPurge.Subscribe(this);
    }
}
package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAfterlifeSubscriber;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Hisako extends AnimatorCard implements OnPurgeSubscriber, OnAfterlifeSubscriber
{
    public static final EYBCardData DATA = Register(Hisako.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public Hisako()
    {
        super(DATA);

        Initialize(0, 7, 3, 2);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetAffinity_General(3);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
        SetAffinity_General(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback((cards) -> {
            if (cards.size() > 0) {
                Affinity af = AffinityToken.GetAffinityFromCardID(cards.get(0).cardID);
                GameActions.Bottom.Draw(1).SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).affinities.GetLevel(af) > 0, false);
            }
        });
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (player.hand.contains(this)) {
            GameActions.Bottom.GainBlessing(1);
        }
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (player.hand.contains(this) && source == player.exhaustPile) {
            GameActions.Bottom.GainBlessing(1);
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
package eatyourbeets.cards.animator.colorless.uncommon;

import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class AffinityToken_General extends AffinityToken implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(AffinityToken_General.class)
            .SetSkill(UNPLAYABLE_COST, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .PostInitialize(data ->
            {
                for (EYBCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });
    public static final Affinity AFFINITY_TYPE = Affinity.Star;

    public AffinityToken_General()
    {
        super(DATA, AFFINITY_TYPE);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetUnplayable(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        GameActions.Top.Exhaust(this);
        CombatStats.Affinities.AddAffinitySealUses(1);
        GameActions.Bottom.SFX(SFX.RELIC_ACTIVATION, 0.75f, 0.85f);
        //GameActions.Bottom.GainBlock(block).SetVFX(true, true);
    }

//    @Override
//    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
//    {
//        GameActions.Bottom.GainBlock(block);
//        GameActions.Bottom.Add(SelectTokenAction(name, upgraded, true, 1, Affinity.Basic().length))
//        .SetOptions(false, false)
//        .AddCallback(m, (enemy, cards) ->
//        {
//            for (AbstractCard c : cards)
//            {
//                c.applyPowers();
//                c.use(player, enemy);
//            }
//        });
//    }

//    @Override
//    public boolean OnAddToDeck()
//    {
//        final CardGroup group = AffinityToken.CreateTokenGroup(AffinityToken.cards.size(), rng);
//
//        GameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
//        .HideTopPanel(true)
//        .SetOptions(false, false)
//        .CancellableFromPlayer(false)
//        .AddCallback(cards ->
//        {
//            for (AbstractCard c : cards)
//            {
//                if (upgraded)
//                {
//                    c.upgrade();
//                }
//
//                GameEffects.TopLevelQueue.ShowAndObtain(c, InputHelper.mX, InputHelper.mY, false);
//            }
//        }));
//
//        return false;
//    }
}
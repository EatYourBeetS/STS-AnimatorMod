package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class KimizugiShiho extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KimizugiShiho.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .AddPreview(new KimizugiMirai(), false);

    public KimizugiShiho()
    {
        super(DATA);

        Initialize(0,6, 5);
        SetUpgrade(0,1, 2);

        SetAffinity_Earth();
        SetAffinity_Mind();

        SetAffinityRequirement(Affinity.Air, 20);
        SetAffinityRequirement(Affinity.Mind, 20);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.RaiseAirLevel(magicNumber);
        GameActions.Bottom.RaiseEarthLevel(magicNumber);

        GameActions.Last.Callback(() -> {
           if (CheckAffinity(Affinity.Air) || CheckAffinity(Affinity.Mind))
           {
               if (CombatStats.TryActivateLimited(cardID))
               {
                   GameActions.Bottom.MakeCardInDiscardPile(new KimizugiMirai());
               }
           }
        });
    }
}
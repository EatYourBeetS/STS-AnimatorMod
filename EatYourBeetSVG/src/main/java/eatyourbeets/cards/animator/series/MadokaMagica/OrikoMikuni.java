package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnBlock;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnDraw;
import eatyourbeets.misc.GenericEffects.GenericEffect_Scry;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrikoMikuni extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrikoMikuni.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 1, 0);

        SetAffinityRequirement(Affinity.Blue, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.PurgeFromPile(name,1,player.exhaustPile).SetOptions(false,true).AddCallback(cards -> {
            if (cards.size() > 0) {
                GameActions.Bottom.GainIntellect(1, true);
            }
        });

        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_Scry(magicNumber));
        choices.AddEffect(new GenericEffect_NextTurnDraw(1));
        choices.AddEffect(new GenericEffect_NextTurnBlock(secondaryValue));

        if (TrySpendAffinity(Affinity.Blue) && info.TryActivateLimited())
        {
            choices.Select(3, m);
        }
        else
        {
            choices.Select(1, m);
        }
    }
}
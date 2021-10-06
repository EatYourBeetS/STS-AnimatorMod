package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_Apply;
import eatyourbeets.misc.GenericEffects.GenericEffect_ChannelOrb;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class CatoElAltestan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CatoElAltestan.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public CatoElAltestan()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(2);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Blue, 2);
        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {


        GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile)
        .SetOptions(false, true)
        .SetFilter(GameUtilities::HasBlueAffinity)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.IncreaseScaling(c, Affinity.Blue, 1);
            }
        });

        choices.Initialize(this, true);
        if (CheckAffinity(Affinity.Blue))
        {
            choices.AddEffect(new GenericEffect_Apply(TargetHelper.Normal(m), PowerHelper.Freezing, secondaryValue));
        }
        if (CheckAffinity(Affinity.Red))
        {
            choices.AddEffect(new GenericEffect_Apply(TargetHelper.Normal(m), PowerHelper.Burning, secondaryValue));
        }
        if (CheckAffinity(Affinity.Green))
        {
            choices.AddEffect(new GenericEffect_ChannelOrb(new Air()));
        }
        choices.Select(1, m);
    }
}
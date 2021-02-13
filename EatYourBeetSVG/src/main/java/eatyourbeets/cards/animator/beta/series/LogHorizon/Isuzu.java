package eatyourbeets.cards.animator.beta.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnDraw;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnEnergy;
import eatyourbeets.utilities.GameActions;

public class Isuzu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Isuzu.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Isuzu()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 1);

        SetExhaust(true);
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        CardEffectChoice choices = new CardEffectChoice();

        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_NextTurnEnergy(magicNumber));
        choices.AddEffect(new GenericEffect_NextTurnDraw(secondaryValue));

        choices.Select(1, m);

        if (HasSynergy())
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
        }
    }
}
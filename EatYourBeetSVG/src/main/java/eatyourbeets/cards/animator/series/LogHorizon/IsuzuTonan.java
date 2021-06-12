package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnDraw;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnEnergy;
import eatyourbeets.utilities.GameActions;

public class IsuzuTonan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsuzuTonan.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsuzuTonan()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_NextTurnEnergy(magicNumber));
        choices.AddEffect(new GenericEffect_NextTurnDraw(secondaryValue));
        choices.Select(GameActions.Bottom, 1, m);

        if (isSynergizing)
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
        }
    }
}
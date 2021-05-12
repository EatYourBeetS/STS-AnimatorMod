package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;

public class MikuIzayoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MikuIzayoi.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public MikuIzayoi()
    {
        super(DATA);

        Initialize(0, 9, 1);
        SetEthereal(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    protected void UpdateBlock(float amount)
    {
        super.UpdateBlock(baseBlock);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_GainStat(1, PlayerAttribute.Force));
            choices.AddEffect(new GenericEffect_GainStat(1, PlayerAttribute.Agility));
            choices.AddEffect(new GenericEffect_GainStat(1, PlayerAttribute.Intellect));
        }

        choices.Select(1, null);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Hisako extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Hisako.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public Soujiro_Hisako()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetEthereal(true);
        SetEvokeOrbCount(1);

        SetSynergy(Synergies.LogHorizon);
        SetAffinity(0, 0, 1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.EvokeOrb(1)
        .AddCallback(orbs ->
        {
            if (orbs.size() > 0)
            {
                GameActions.Bottom.GainIntellect(1, true);
            }
        });
    }
}
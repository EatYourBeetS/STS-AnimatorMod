package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Shinoa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shinoa.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL);

    public Shinoa()
    {
        super(DATA);

        Initialize(0, 6, 1);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.OwariNoSeraph);
        SetAffinity(1, 1, 0, 1, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);

        if (isSynergizing)
        {
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
        }
    }
}
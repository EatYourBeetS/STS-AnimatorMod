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
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);

        if (HasSynergy())
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber)
            .AddCallback(power -> GameActions.Top.ApplyWeak(player, power.owner, magicNumber));
        }
        else
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }
}
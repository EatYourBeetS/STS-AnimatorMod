package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.SilencedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Yae extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yae.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.HoukaiGakuen);

    public Yae()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Dark(1);
        SetEthereal(true);
        SetPurge(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int lightningCount = 0;
        for (AbstractMonster enemy : GameUtilities.GetEnemies(true)) {
            GameActions.Bottom.StackPower(new SilencedPower(enemy, magicNumber));
        }
        GameActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential)
                .SetFilter(o -> Lightning.ORB_ID.equals(o.ID) || (upgraded && Dark.ORB_ID.equals(o.ID)))
                .AddCallback(orbs ->
                {
                    if (orbs.size() == 0)
                    {
                        GameActions.Bottom.ApplyBlinded(TargetHelper.AllCharacters(), secondaryValue);
                    }
                    else
                    {
                        GameActions.Bottom.ChannelOrb(new Frost());
                    }
                });
    }
}
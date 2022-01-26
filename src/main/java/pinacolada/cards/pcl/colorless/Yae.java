package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.*;
import pinacolada.powers.special.SilencedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yae extends PCLCard
{
    public static final PCLCardData DATA = Register(Yae.class).SetSkill(1, CardRarity.RARE, PCLCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.HoukaiGakuen);

    public Yae()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
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
        for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Bottom.StackPower(new SilencedPower(enemy, magicNumber));
        }
        PCLActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential)
                .SetFilter(o -> Lightning.ORB_ID.equals(o.ID) || (upgraded && Dark.ORB_ID.equals(o.ID)))
                .AddCallback(orbs ->
                {
                    if (orbs.size() == 0)
                    {
                        PCLActions.Bottom.ApplyBlinded(TargetHelper.AllCharacters(), secondaryValue);
                    }
                    else
                    {
                        PCLActions.Bottom.ChannelOrb(new Frost());
                    }
                });
    }
}
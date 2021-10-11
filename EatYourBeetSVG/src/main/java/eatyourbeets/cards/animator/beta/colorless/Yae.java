package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.Amplification_LightningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Yae extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yae.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.HoukaiGakuen);

    public Yae()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
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
        GameActions.Bottom.StackPower(new Amplification_LightningPower(p, 1));
        for (AbstractOrb orb : p.orbs)
        {
            if (!(orb instanceof EmptyOrbSlot) && orb.ID.equals(Lightning.ORB_ID))
            {
                GameActions.Bottom.Add(new RemoveOrb(orb));
                GameActions.Bottom.RaiseWaterLevel(magicNumber, upgraded);
                if (++lightningCount >= magicNumber) {
                    break;
                }

            }
        }

        if (lightningCount == 0) {
            GameActions.Bottom.ChannelOrb(new Dark());
            GameActions.Bottom.ApplyBlinded(TargetHelper.AllCharacters(), secondaryValue);
            GameActions.Bottom.ApplyFrail(TargetHelper.AllCharacters(), secondaryValue);
        }
    }
}
package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.orbs.pcl.Water;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AkariMizunashi extends PCLCard
{
    public static final PCLCardData DATA = Register(AkariMizunashi.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.Self)
            .SetMaxCopies(1)
            .SetMultiformData(2, false)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Aria);

    public AkariMizunashi()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0,0,0,0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(auxiliaryData.form == 1 ? cardData.Strings.EXTENDED_DESCRIPTION[0] : "");
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0 && form == 0) {
            SetRetainOnce(true);
            Initialize(0, 0, 6, 3);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID, secondaryValue)) {
            PCLActions.Bottom.Heal(secondaryValue);
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public int GetXValue() {
        return secondaryValue - PCLCombatStats.GetLimitedActivations(cardID);
    }

    public AbstractAttribute GetSpecialInfo() {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        if (auxiliaryData.form == 1) {
            for (int i = 0; i < player.orbs.size(); i++) {
                AbstractOrb orb = player.orbs.get(i);
                if (PCLGameUtilities.IsValidOrb(orb) && Frost.ORB_ID.equals(orb.ID)) {
                    Water water = new Water();
                    water.SetBasePassiveAmount(PCLGameUtilities.GetOrbBasePassiveAmount(orb), false);
                    water.SetBaseEvokeAmount(PCLGameUtilities.GetOrbBaseEvokeAmount(orb), false);
                    water.evokeAmount = water.GetBaseEvokeAmount();
                    player.orbs.set(i, water);
                }
            }
        }

        PCLActions.Bottom.EvokeOrb(1, EvokeOrb.Mode.SameOrb)
                .SetFilter(o -> Water.ORB_ID.equals(o.ID))
                .AddCallback(orbs ->
                {
                    if (orbs.size() > 0)
                    {
                        PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Light, PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Light));
                    }
                    else
                    {
                        PCLActions.Bottom.ChannelOrb(new Water());
                    }
                });
    }
}
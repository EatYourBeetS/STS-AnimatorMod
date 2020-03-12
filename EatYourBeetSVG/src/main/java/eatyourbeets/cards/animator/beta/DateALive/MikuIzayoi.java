package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;

public class MikuIzayoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MikuIzayoi.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);
    public static final EYBCardTooltip CommonBuffs = new EYBCardTooltip(DATA.Strings.EXTENDED_DESCRIPTION[0], DATA.Strings.EXTENDED_DESCRIPTION[1]);

    public static final int BLOCK_AMOUNT = 9;

    public MikuIzayoi()
    {
        super(DATA);

        Initialize(0, BLOCK_AMOUNT);
        SetUpgrade(0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(CommonBuffs);
        }
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return BLOCK_AMOUNT;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        for (AbstractPower power : player.powers)
        {
            if (ForcePower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainForce(1);
            }
            else if (AgilityPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainAgility(1);
            }
            else if (IntellectPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainIntellect(1);
            }
            else if (ThornsPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainThorns(1);
            }
            else if (EarthenThornsPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainTemporaryThorns(1);
            }
            else if (MetallicizePower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainMetallicize(1);
            }
            else if (PlatedArmorPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainPlatedArmor(1);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }
}
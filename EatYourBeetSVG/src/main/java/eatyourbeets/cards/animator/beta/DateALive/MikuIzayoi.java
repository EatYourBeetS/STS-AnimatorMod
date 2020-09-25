package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MikuIzayoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MikuIzayoi.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);
    public static final EYBCardTooltip CommonBuffs = new EYBCardTooltip(DATA.Strings.EXTENDED_DESCRIPTION[0], DATA.Strings.EXTENDED_DESCRIPTION[1]);

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
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(CommonBuffs);
        }
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

        if (GameUtilities.InStance(NeutralStance.STANCE_ID))
        {
            for (AbstractPower power : player.powers)
            {
                if (ForcePower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainForce(magicNumber);
                }
                else if (AgilityPower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainAgility(magicNumber);
                }
                else if (IntellectPower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainIntellect(magicNumber);
                }
                else if (ThornsPower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainThorns(magicNumber);
                }
                else if (EarthenThornsPower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainTemporaryThorns(magicNumber);
                }
                else if (MetallicizePower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainMetallicize(magicNumber);
                }
                else if (PlatedArmorPower.POWER_ID.equals(power.ID))
                {
                    GameActions.Bottom.GainPlatedArmor(magicNumber);
                }
            }
        }
        else
        {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }
}
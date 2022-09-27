package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.SayakaMiki_Oktavia;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SayakaMiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SayakaMiki.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.MadokaMagica_Witch(SayakaMiki_Oktavia.DATA));
                data.AddPreview(new SayakaMiki_Oktavia(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
            });

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 2);

        SetAffinity_Green(1, 1, 0);
        SetAffinity_Blue(2);

        SetAffinityRequirement(Affinity.Light, 1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new SayakaMikiPower(p, 1)).ShowEffect(false, false);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.RecoverHP(secondaryValue);
        }
        else
        {
            GameActions.Bottom.GainBlessing(1, true);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetHealthRecoverAmount(secondaryValue) > 0 && super.CheckSpecialCondition(tryUse);
    }

    public static class SayakaMikiPower extends AnimatorPower
    {
        public SayakaMikiPower(AbstractCreature owner, int amount)
        {
            super(owner, SayakaMiki.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.ChannelOrbs(Frost::new, amount);
            RemovePower();
        }
    }
}

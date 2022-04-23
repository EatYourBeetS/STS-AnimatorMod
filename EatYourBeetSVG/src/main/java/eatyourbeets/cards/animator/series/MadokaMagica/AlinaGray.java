package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class AlinaGray extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AlinaGray.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public AlinaGray()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);

        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Green(1);
        SetAffinity_Dark(1);

        SetDelayed(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .SetFilter(GameUtilities::IsHindrance)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.Draw(1);
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Poison, secondaryValue);
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, secondaryValue);
            }
        });
        GameActions.Bottom.StackPower(new AlinaGrayPower(p, magicNumber));
    }

    public static class AlinaGrayPower extends AnimatorPower
    {
        public AlinaGrayPower(AbstractCreature owner, int amount)
        {
            super(owner, AlinaGray.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public void onChannel(AbstractOrb orb)
        {
            super.onChannel(orb);

            GameActions.Bottom.StackPower(owner, new NoxiousFumesPower(owner, 1));
            flashWithoutSound();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}
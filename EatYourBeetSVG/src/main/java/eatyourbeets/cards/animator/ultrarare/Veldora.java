package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Veldora extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Veldora.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TenseiSlime);

    public Veldora()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), secondaryValue)
                    .ShowEffect(false, true);
        }

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!p.hasPower(PlayerFlightPower.POWER_ID))
        {
            GameActions.Bottom.StackPower(new PlayerFlightPower(p, 2));
        }

        GameActions.Bottom.StackPower(new VeldoraPower(p, 1));
    }

    public class VeldoraPower extends AnimatorPower
    {
        public VeldoraPower(AbstractCreature owner, int amount)
        {
            super(owner, Veldora.DATA);

            Initialize(amount);
        }

        public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(p, target, source);

            if (p.type == PowerType.DEBUFF && !p.ID.equals(GainStrengthPower.POWER_ID) && source == owner && !target.hasPower(ArtifactPower.POWER_ID))
            {
                GameActions.Bottom.GainRandomAffinityPower(1, true);
                this.flash();
            }
        }
    }
}
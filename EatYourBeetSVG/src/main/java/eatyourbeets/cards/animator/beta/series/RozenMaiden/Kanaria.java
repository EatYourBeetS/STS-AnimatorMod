package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.cards.animator.beta.special.Kanaria_Pizzicato;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kanaria extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kanaria.class)
    		.SetPower(2, CardRarity.RARE).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Kanaria_Pizzicato(), false));

    public Kanaria()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Green(1, 0, 0);

        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new KanariaPower(p, 1));

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new Kanaria_Pizzicato())
                    .AddCallback(GameUtilities::Retain);
        }
    }

    public static class KanariaPower extends AnimatorPower
    {
        public static final int BASE_BLOCK = 2;

        public KanariaPower(AbstractCreature owner, int amount)
        {
            super(owner, Kanaria.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount * BASE_BLOCK);
        }

        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            if (power.type == PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) &&
                    source == this.owner && !target.hasPower(ArtifactPower.POWER_ID))
            {
                this.flash();
                GameActions.Bottom.GainBlock(amount * BASE_BLOCK);
            }
        }
    }
}
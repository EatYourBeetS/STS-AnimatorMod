package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class LuciaKonohana extends AnimatorCard {
    public static final EYBCardData DATA = Register(LuciaKonohana.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public static final EYBCardTooltip CommonDebuffs = new EYBCardTooltip(DATA.Strings.EXTENDED_DESCRIPTION[1], DATA.Strings.EXTENDED_DESCRIPTION[2]);

    public LuciaKonohana() {
        super(DATA);

        Initialize(0, 0, 7);
        SetUpgrade(0, 0, 3);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(CommonDebuffs);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster randomEnemy = GameUtilities.GetRandomEnemy(true);

        GameActions.Bottom.ApplyPoison(p, randomEnemy, magicNumber);

        if (player.stance.ID.equals(AgilityStance.STANCE_ID))
        {
            GameActions.Bottom.ApplyPower(p, randomEnemy, new LuciaKonohanaPower(p, 1));
        }
    }

    public static class LuciaKonohanaPower extends AnimatorPower
    {
        public LuciaKonohanaPower(AbstractCreature owner, int amount) {
            super(owner, LuciaKonohana.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0);
        }

        @Override
        public void onDeath()
        {
            final AbstractCreature corpse = this.owner;
            int amount;

            for (AbstractPower debuff : corpse.powers)
            {
                if (WeakPower.POWER_ID.equals(debuff.ID))
                {
                    amount = GameUtilities.GetPowerAmount(corpse, WeakPower.POWER_ID);
                    GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), amount);
                }
                else if (VulnerablePower.POWER_ID.equals(debuff.ID))
                {
                    amount = GameUtilities.GetPowerAmount(corpse, VulnerablePower.POWER_ID);
                    GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), amount);
                }
                else if (PoisonPower.POWER_ID.equals(debuff.ID))
                {
                    amount = GameUtilities.GetPowerAmount(corpse, PoisonPower.POWER_ID);
                    GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), amount);
                }
                else if (BurningPower.POWER_ID.equals(debuff.ID))
                {
                    amount = GameUtilities.GetPowerAmount(corpse, BurningPower.POWER_ID);
                    GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), amount);
                }
                else if (GainStrengthPower.POWER_ID.equals(debuff.ID))
                {
                    amount = GameUtilities.GetPowerAmount(corpse, GainStrengthPower.POWER_ID);
                    for (AbstractCreature enemy : GameUtilities.GetEnemies(true))
                    {
                        GameActions.Bottom.ReduceStrength(enemy, amount, true);
                    }
                }
            }
        }
    }
}
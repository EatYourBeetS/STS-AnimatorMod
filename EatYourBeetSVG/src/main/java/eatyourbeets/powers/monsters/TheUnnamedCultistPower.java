package eatyourbeets.powers.monsters;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamedCultistPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TheUnnamedCultistPower.class);

    public TheUnnamedCultistPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActions.Bottom.RemovePower(owner, owner, EnchantedArmorPower.POWER_ID);

        int count = GameUtilities.GetEnemies(true).size() - 1;
        if (count > 0)
        {
            GameActions.Bottom.StackPower(new EnchantedArmorPower(owner, amount * count))
            .ShowEffect(false, true);
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        if (usedCard instanceof SummoningRitual && !owner.isPlayer)
        {
            GameActions.Bottom.Talk(owner, TheUnnamed_Cultist.STRINGS.DIALOG[12]);
            GameActions.Bottom.ApplyPower(new StunMonsterPower((AbstractMonster) owner));
        }
    }
}

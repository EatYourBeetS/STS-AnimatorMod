package eatyourbeets.powers.monsters;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamedCultistPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TheUnnamedCultistPower.class);

    private boolean stunned;

    public TheUnnamedCultistPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
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
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        if (card instanceof SummoningRitual && owner instanceof AbstractMonster && !stunned)
        {
            GameActions.Bottom.Talk(owner, TheUnnamed_Cultist.STRINGS.DIALOG[11]);
            GameActions.Bottom.ApplyPower(new StunMonsterPower((AbstractMonster) owner));
            stunned = true;
        }
    }
}

package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public abstract class AbstractTemporaryPower extends CommonPower
{
    public static final String ID = CreateFullID(AbstractTemporaryPower.class);

    private final String targetName;
    private final FuncT2<AbstractPower, AbstractCreature, Integer> constructor;

    public AbstractTemporaryPower(AbstractCreature owner, int amount, String powerID, FuncT2<AbstractPower, AbstractCreature, Integer> constructor)
    {
        super(owner, powerID);

        this.constructor = constructor;

        AbstractPower targetPower = constructor.Invoke(owner, amount);
        this.targetName = targetPower.name;
        this.img = targetPower.img;
        this.powerIcon = targetPower.region128;
        this.useTemporaryColoring = true;
        this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
        Initialize(amount, targetPower.type, true);

        updateDescription();
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        GameActions.Top.StackPower(constructor.Invoke(owner, difference));

        super.onAmountChanged(previousAmount, difference);
    }

    @Override
    public void atStartOfTurn()
    {
        if (amount < 0) {
            GameActions.Top.StackPower(constructor.Invoke(owner, -amount));
        }
        RemovePower();
    }

    @Override
    public void updateDescription() {
        this.description = amount < 0 ? FormatDescription(1, -amount, targetName) : FormatDescription(0, amount, targetName);
    }
}
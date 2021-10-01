package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public abstract class AbstractTemporaryPower extends CommonPower
{
    public static final String ID = CreateFullID(AbstractTemporaryPower.class);

    private final String targetPowerID;
    private final AbstractPower targetPower;

    public AbstractTemporaryPower(AbstractCreature owner, int amount, String powerID, FuncT2<AbstractPower, AbstractCreature, Integer> constructor)
    {
        super(owner, powerID);

        this.targetPower = constructor.Invoke(owner, amount);
        this.targetPowerID = this.targetPower.ID;
        this.img = this.targetPower.img;
        this.powerIcon = this.targetPower.region128;
        this.useTemporaryColoring = true;
        this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
        Initialize(amount, this.targetPower.type, true);

        updateDescription();
    }

    public AbstractTemporaryPower(AbstractCreature owner, AbstractCreature source, int amount, String powerID, FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructor)
    {
        super(owner, powerID);

        this.amount = amount;
        this.targetPower = constructor.Invoke(owner, source, amount);
        this.targetPowerID = this.targetPower.ID;
        this.type = this.targetPower.type;
        this.img = this.targetPower.img;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.StackPower(owner, this.targetPower);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        GameActions.Bottom.StackPower(owner, this.targetPower);
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.ReducePower(owner, owner, targetPowerID, this.amount);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription() {
        this.description =  FormatDescription(0, amount, targetPower.name);
    }
}
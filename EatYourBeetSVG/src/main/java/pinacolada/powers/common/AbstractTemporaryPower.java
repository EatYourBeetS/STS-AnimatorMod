package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public abstract class AbstractTemporaryPower extends PCLPower
{
    public static final String ID = CreateFullID(AbstractTemporaryPower.class);

    private final String targetName;
    private final FuncT1<AbstractPower, AbstractCreature> constructorT1;
    private final FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2;

    public AbstractTemporaryPower(AbstractCreature owner, int amount, String powerID, FuncT1<AbstractPower, AbstractCreature> constructorT1) {
        this(owner,amount,powerID,constructorT1,null);
    }

    public AbstractTemporaryPower(AbstractCreature owner, int amount, String powerID, FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2) {
        this(owner,amount,powerID,null,constructorT2);
    }

    public AbstractTemporaryPower(AbstractCreature owner, int amount, String powerID, FuncT1<AbstractPower, AbstractCreature> constructorT1, FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2)
    {
        super(owner, powerID);

        this.constructorT1 = constructorT1;
        this.constructorT2 = constructorT2;


        AbstractPower targetPower = constructorT1 != null ? constructorT1.Invoke(owner) : constructorT2.Invoke(owner, amount);
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
        if (constructorT2 != null) {
            PCLActions.Top.StackPower(constructorT2.Invoke(owner, difference));
        }
        else if (constructorT1 != null && previousAmount + difference != 0) {
            PCLActions.Top.StackPower(constructorT1.Invoke(owner));
        }

        super.onAmountChanged(previousAmount, difference);
    }

    @Override
    public void atStartOfTurn()
    {
        if (amount < 0 && constructorT2 != null) {
            PCLActions.Top.StackPower(constructorT2.Invoke(owner, -amount));
        }
        RemovePower();
    }

    @Override
    public void updateDescription() {
        this.description = amount < 0 ? FormatDescription(1, -amount, targetName) : FormatDescription(0, amount, targetName);
        this.name = FormatDescription(2, targetName);
    }
}
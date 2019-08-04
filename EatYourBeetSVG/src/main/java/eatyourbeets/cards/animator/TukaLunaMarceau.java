package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.HandSizePower;
import eatyourbeets.utilities.GameActionsHelper;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final String ID = CreateFullID(TukaLunaMarceau.class.getSimpleName());

    private int handSizeReduction = 0;

    public TukaLunaMarceau()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 2, 0, 3);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        if (p.drawPile.size() == 0)
        {
            GameActionsHelper.AddToBottom(new EmptyDeckShuffleAction());
        }
        GameActionsHelper.DrawCard(p, 1);
        GameActionsHelper.ApplyPowerSilently(p, p, new HandSizePower(p, -secondaryValue), -secondaryValue);
        //GameActionsHelper.AddToBottom(new ReduceHandSizeAction(this));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeSecondaryValue(-1);
        }
    }
}
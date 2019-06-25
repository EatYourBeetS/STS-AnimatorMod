package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ModifyBlockActionWhichActuallyWorks;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shuna extends AnimatorCard
{
    public static final String ID = CreateFullID(Shuna.class.getSimpleName());

    public Shuna()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,2, 1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper.Callback(new WaitAction(0.1f), this::OnCompletion, this);
    }

    private void OnCompletion(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            this.applyPowers();
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.GainBlock(p, this.block);
            GameActionsHelper.GainBlock(p, this.block);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, 1);
        GameActionsHelper.AddToBottom(new ModifyBlockActionWhichActuallyWorks(this.uuid, magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
        }
    }
}
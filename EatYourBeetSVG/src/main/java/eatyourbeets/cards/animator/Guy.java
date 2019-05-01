package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.StrategicInformationPower;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 1);

        AddExtendedDescription();

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.Callback(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber, false), this::OnDiscard, this);
        }
        else
        {
            GameActionsHelper.ChooseAndDiscard(this.magicNumber, false);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void OnDiscard(Object state, AbstractGameAction action)
    {
        if (state == this)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.ApplyPower(p, p, new StrategicInformationPower(p, 1), 1);
        }
    }
}
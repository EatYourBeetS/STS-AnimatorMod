package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Togame extends AnimatorCard
{
    public static final String ID = CreateFullID(Togame.class.getSimpleName());

    public Togame()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        AddExtendedDescription();

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new DrawCardAction(p, this.magicNumber));
        GameActionsHelper.AddToBottom(new TogameAction(this));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    public class TogameAction extends AnimatorAction
    {
        private final Togame togame;

        public TogameAction(Togame togame)
        {
            this.togame = togame;
        }

        @Override
        public void update()
        {
            AbstractPlayer p = AbstractDungeon.player;
            int cards = p.hand.size();
            switch (cards)
            {
                case 7:
                    GameActionsHelper.AddToBottom(new ExhaustAction(p, p, 1, false, false, false));
                    break;

                case 6:
                    GameActionsHelper.GainBlock(p, 6);
                    break;

                case 5:
                    GameActionsHelper.ApplyPower(p, p, new ArtifactPower(p, 1), 1);
                    break;

                case 4:
                    GameActionsHelper.GainEnergy(2);
                    GameActionsHelper.ExhaustCard(togame, p.discardPile);
                    GameActionsHelper.ExhaustCard(togame, p.drawPile);
                    GameActionsHelper.ExhaustCard(togame, p.hand);
                    GameActionsHelper.ExhaustCard(togame, p.limbo);
                    break;
            }

            this.isDone = true;
        }
    }
}
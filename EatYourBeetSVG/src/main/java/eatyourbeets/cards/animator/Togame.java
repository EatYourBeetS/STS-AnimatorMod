package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.actions.OptionalNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Togame extends AnimatorCard
{
    public static final String ID = CreateFullID(Togame.class.getSimpleName());
    private static final String cardDrawString = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.Actions).TEXT[4];

    private int cardDraw;

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
        cardDraw = this.baseMagicNumber;
        GameActionsHelper.AddToBottom(new OptionalNumberAction(this, cardStrings.EXTENDED_DESCRIPTION[2],
                                                                    this::UpdateCurrentText, this::OnEffectSelected));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private String UpdateCurrentText(Integer shift)
    {
        int n = shift == null ? 0 : shift;
        int remainder = (cardDraw + n) % (baseMagicNumber + 1);
        if (remainder < 0)
        {
            cardDraw = remainder + (baseMagicNumber + 1);
        }
        else
        {
            cardDraw = remainder;
        }

        return cardDrawString.replace("#", String.valueOf(cardDraw));
    }

    private void OnEffectSelected(AbstractCard card)
    {
        if (card == this)
        {
            GameActionsHelper.DrawCard(AbstractDungeon.player, cardDraw);
            GameActionsHelper.AddToBottom(new TogameAction(this));
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
            if (cards == 4)
            {
                GameActionsHelper.GainEnergy(2);
                GameActionsHelper.ExhaustCard(togame, p.discardPile);
                GameActionsHelper.ExhaustCard(togame, p.drawPile);
                GameActionsHelper.ExhaustCard(togame, p.hand);
                GameActionsHelper.ExhaustCard(togame, p.limbo);
            }
            else if (cards == 5)
            {
                GameActionsHelper.ApplyPower(p, p, new ArtifactPower(p, 1), 1);
            }
            else if (cards == 6)
            {
                GameActionsHelper.GainBlock(p, 6);
            }
            else if (cards >= 7)
            {
                GameActionsHelper.AddToBottom(new ExhaustAction(p, p, 1, false, false, false));
            }

            this.isDone = true;
        }
    }
}
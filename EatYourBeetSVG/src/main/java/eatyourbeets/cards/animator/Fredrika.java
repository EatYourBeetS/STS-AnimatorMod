package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ChooseFromPileAction;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Fredrika extends AnimatorCard
{
    public static final String ID = CreateFullID(Fredrika.class.getSimpleName());

    private static final int FORM_DEFAULT = 0;
    private static final int FORM_CAT = 1;
    private static final int FORM_DOMINICA = 2;
    private static final int FORM_DRAGOON = 3;

    private int currentForm = 0;

    public Fredrika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(9, 4);

        SetSynergy(Synergies.Chaika, true);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        this.ChangeForm(FORM_DEFAULT);
    }

    @Override
    public void onMoveToDiscard()
    {
        super.onMoveToDiscard();

        this.ChangeForm(FORM_DEFAULT);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (this.currentForm == FORM_DEFAULT)
        {
            CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (int i = 1; i <= 3; i++)
            {
                Fredrika other = (Fredrika) makeStatEquivalentCopy();
                other.ChangeForm(i);
                cardGroup.addToTop(other);
            }

            ChooseFromPileAction action = new ChooseFromPileAction(1, false, cardGroup, this::OnCardSelected, this, CardRewardScreen.TEXT[1], true);

            GameActionsHelper.AddToBottom(action);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        switch (currentForm)
        {
            case FORM_DEFAULT:
            {
                for (AbstractMonster m1 : AbstractDungeon.getCurrRoom().monsters.monsters)
                {
                    if (!m1.isDying && m1.currentHealth > 0)
                    {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block, true));
                    }
                }

                break;
            }

            case FORM_CAT:
            {
                GameActionsHelper.GainBlock(p, this.block);

                break;
            }

            case FORM_DOMINICA:
            {
                GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
                GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);

                break;
            }

            case FORM_DRAGOON:
            {
                GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, 2), 2);

                break;
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeDamage(2);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Fredrika other = (Fredrika) super.makeStatEquivalentCopy();

        other.ChangeForm(this.currentForm);

        return other;
    }

    private void OnCardSelected(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null && cards.size() == 1)
        {
            Fredrika selected = (Fredrika) cards.get(0);

            this.ChangeForm(selected.currentForm);

            AbstractPlayer p = AbstractDungeon.player;

            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand, p.discardPile));
            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand, p.drawPile));
        }
    }

    private void ChangeForm(int formID)
    {
        if (this.currentForm == formID)
        {
            return;
        }

        this.currentForm = formID;

        switch (formID)
        {
            case FORM_DEFAULT:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID));
                this.rawDescription = cardStrings.DESCRIPTION;
                this.type = CardType.SKILL;
                this.target = CardTarget.SELF;
                this.cost = 1;

                break;
            }

            case FORM_CAT:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_Cat"));
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
                this.type = CardType.SKILL;
                this.target = CardTarget.NONE;
                this.cost = 0;

                break;
            }

            case FORM_DRAGOON:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_Dragoon"));
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                this.cost = 2;

                break;
            }

            case FORM_DOMINICA:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_Dominica"));
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[2];
                this.type = CardType.ATTACK;
                this.target = CardTarget.ENEMY;
                this.cost = 1;

                break;
            }
        }

        this.setCostForTurn(cost);
        this.initializeDescription();
    }
}

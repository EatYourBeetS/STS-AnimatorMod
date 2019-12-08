package eatyourbeets.cards.animator;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions._legacy.common.RefreshHandLayoutAction;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.actions._legacy.common.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Fredrika extends AnimatorCard
{
    public static final String ID = Register(Fredrika.class.getSimpleName(), EYBCardBadge.Discard);

    private static final int FORM_DEFAULT = 0;
    private static final int FORM_CAT = 1;
    private static final int FORM_DOMINICA = 2;
    private static final int FORM_DRAGOON = 3;

    private int currentForm = 0;

    public Fredrika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(9, 2, 2);

        if (InitializingPreview())
        {
            Fredrika c1 = new Fredrika();
            Fredrika c2 = new Fredrika();

            c1.ChangeForm(FORM_DRAGOON);
            c2.ChangeForm(FORM_DRAGOON);
            c2.upgrade();

            cardData.InitializePreview(c1, c2);
        }

        SetSynergy(Synergies.Chaika, true);
    }

    @SpireOverride
    protected void applyPowersToBlock()
    {
        float tmp = (float) this.baseBlock;

        if (currentForm == FORM_DEFAULT)
        {
            tmp += GameUtilities.GetCurrentEnemies(true).size() * magicNumber;
        }

        for (AbstractPower p : AbstractDungeon.player.powers)
        {
            tmp = p.modifyBlock(tmp);
        }

        if (tmp < 0.0F)
        {
            tmp = 0.0F;
        }

        this.block = MathUtils.floor(tmp);
        this.isBlockModified = (this.baseBlock != this.block);
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
                GameActionsHelper2.GainBlock(block);

                break;
            }

            case FORM_CAT:
            {
                GameActionsHelper2.GainBlock(block);

                break;
            }

            case FORM_DOMINICA:
            {
                GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
                GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);

                break;
            }

            case FORM_DRAGOON:
            {
                GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
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
            //upgradeMagicNumber(1);
            upgradeBlock(2);
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

            GameActionsHelper.AddToBottom(new MoveCard(this, p.hand));
            GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
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
                this.cardText.OverrideDescription(null, true);
                this.type = CardType.SKILL;
                this.target = CardTarget.SELF;
                this.cost = 1;

                break;
            }

            case FORM_CAT:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_Cat"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
                this.type = CardType.SKILL;
                this.target = CardTarget.NONE;
                this.cost = 0;

                break;
            }

            case FORM_DRAGOON:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_Dragoon"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[1], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                this.cost = 2;

                break;
            }

            case FORM_DOMINICA:
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_Dominica"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[2], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.ENEMY;
                this.cost = 1;

                break;
            }
        }

        this.setCostForTurn(cost);
    }
}

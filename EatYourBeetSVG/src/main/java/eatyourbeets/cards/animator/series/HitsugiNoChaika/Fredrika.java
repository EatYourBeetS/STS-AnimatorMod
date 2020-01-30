package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Fredrika extends AnimatorCard
{
    private enum Form
    {
        Default,
        Cat,
        Dominica,
        Dragoon
    }

    public static final String ID = Register(Fredrika.class);
    static
    {
        staticCardData.get(ID).InitializePreview(new Fredrika(Form.Dragoon), true);
    }

    private Form currentForm = Form.Default;
    private Fredrika(Form form)
    {
        this();

        ChangeForm(form);
    }

    public Fredrika()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(9, 2, 2);
        SetUpgrade(2, 2, 0);

        SetSynergy(Synergies.Chaika, true);
    }

    @Override
    protected float GetInitialBlock()
    {
        if (currentForm == Form.Default)
        {
            return super.GetInitialBlock() + GameUtilities.GetCurrentEnemies(true).size() * magicNumber;
        }

        return super.GetInitialBlock();
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        this.ChangeForm(Form.Default);
    }

    @Override
    public void onMoveToDiscard()
    {
        super.onMoveToDiscard();

        this.ChangeForm(Form.Default);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (this.currentForm == Form.Default)
        {
            CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (int i = 1; i <= 3; i++)
            {
                Fredrika other = (Fredrika) makeStatEquivalentCopy();
                other.ChangeForm(Form.values()[i]);
                cardGroup.addToTop(other);
            }

            GameActions.Bottom.SelectFromPile(name, 1, cardGroup)
            .SetOptions(false, false)
            .SetMessage(CardRewardScreen.TEXT[1])
            .AddCallback(cards ->
            {
                this.ChangeForm(((Fredrika) cards.get(0)).currentForm);

                GameActions.Bottom.Add(new RefreshHandLayout());
                GameActions.Top.MoveCard(this, AbstractDungeon.player.hand);
            });
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        switch (currentForm)
        {
            case Default:
            {
                GameActions.Bottom.GainBlock(block);

                break;
            }

            case Cat:
            {
                GameActions.Bottom.GainBlock(block);

                break;
            }

            case Dominica:
            {
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
                GameActions.Bottom.ApplyWeak(p, m, 1);

                break;
            }

            case Dragoon:
            {
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActions.Bottom.GainMetallicize(2);

                break;
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Fredrika other = (Fredrika) super.makeStatEquivalentCopy();

        other.ChangeForm(this.currentForm);

        return other;
    }

    private void ChangeForm(Form formID)
    {
        if (this.currentForm == formID)
        {
            return;
        }

        this.currentForm = formID;

        switch (formID)
        {
            case Default:
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID));
                this.cardText.OverrideDescription(null, true);
                this.type = CardType.SKILL;
                this.target = CardTarget.SELF;
                this.cost = 1;

                break;
            }

            case Cat:
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_Cat"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
                this.type = CardType.SKILL;
                this.target = CardTarget.NONE;
                this.cost = 0;

                break;
            }

            case Dragoon:
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_Dragoon"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[1], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                this.cost = 2;

                break;
            }

            case Dominica:
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_Dominica"));
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

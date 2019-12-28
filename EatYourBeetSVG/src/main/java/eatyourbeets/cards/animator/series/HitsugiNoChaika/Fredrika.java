package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Fredrika extends AnimatorCard
{
    public static final String ID = Register(Fredrika.class, EYBCardBadge.Discard);

    private static final int FORM_DEFAULT = 0;
    private static final int FORM_CAT = 1;
    private static final int FORM_DOMINICA = 2;
    private static final int FORM_DRAGOON = 3;

    private int currentForm = 0;

    public Fredrika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(9, 2, 2);
        SetUpgrade(2, 2, 0);

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
            case FORM_DEFAULT:
            {
                GameActions.Bottom.GainBlock(block);

                break;
            }

            case FORM_CAT:
            {
                GameActions.Bottom.GainBlock(block);

                break;
            }

            case FORM_DOMINICA:
            {
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
                GameActions.Bottom.ApplyWeak(p, m, 1);

                break;
            }

            case FORM_DRAGOON:
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
                this.loadCardImage(AnimatorResources.GetCardImage(ID));
                this.cardText.OverrideDescription(null, true);
                this.type = CardType.SKILL;
                this.target = CardTarget.SELF;
                this.cost = 1;

                break;
            }

            case FORM_CAT:
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_Cat"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
                this.type = CardType.SKILL;
                this.target = CardTarget.NONE;
                this.cost = 0;

                break;
            }

            case FORM_DRAGOON:
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_Dragoon"));
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[1], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                this.cost = 2;

                break;
            }

            case FORM_DOMINICA:
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

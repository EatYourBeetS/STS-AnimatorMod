package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Fredrika extends AnimatorCard implements OnEndOfTurnSubscriber //TODO use multiform
{
    private enum Form
    {
        Default,
        Cat,
        Dominica,
        Dragoon
    }

    public static final EYBCardData DATA = Register(Fredrika.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Fredrika(Form.Cat), true);
                data.AddPreview(new Fredrika(Form.Dominica), true);
                data.AddPreview(new Fredrika(Form.Dragoon), true);
            });

    private Form currentForm = Form.Default;

    private Fredrika(Form form)
    {
        this();

        ChangeForm(form);
    }

    public Fredrika()
    {
        super(DATA);

        Initialize(9, 2, 2);
        SetUpgrade(2, 2, 0);

        SetAffinity_Star(1, 0, 1);

        SetAttackType(EYBAttackType.Normal);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (currentForm == Form.Dominica && m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        if (currentForm != Form.Default)
        {
            return null;
        }

        return super.GetCardPreview();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (currentForm == Form.Dominica || currentForm == Form.Dragoon)
        {
            return super.GetDamageInfo();
        }
        return null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (currentForm == Form.Default || currentForm == Form.Cat)
        {
            return super.GetBlockInfo();
        }

        return null;
    }

    @Override
    protected float GetInitialBlock()
    {
        if (currentForm == Form.Default)
        {
            return super.GetInitialBlock() + GameUtilities.GetEnemies(true).size() * magicNumber;
        }

        return super.GetInitialBlock();
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
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
            .AddCallback(cards ->
            {
                ChangeForm(((Fredrika) cards.get(0)).currentForm);

                GameActions.Bottom.Add(new RefreshHandLayout());
                GameActions.Top.MoveCard(this, player.hand);
            });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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
                GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
                GameActions.Bottom.ApplyWeak(p, m, 1);
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
                break;
            }

            case Dragoon:
            {
                GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
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
                LoadImage(null);

                this.cardText.OverrideDescription(null, true);
                this.type = CardType.SKILL;
                this.target = CardTarget.SELF;
                this.cost = 1;
                SetHitCount(1);

                break;
            }

            case Cat:
            {
                LoadImage("_Cat");

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                this.type = CardType.SKILL;
                this.target = CardTarget.NONE;
                this.cost = 0;
                SetHitCount(1);

                break;
            }

            case Dragoon:
            {
                LoadImage("_Dragoon");

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                this.cost = 2;
                SetHitCount(2);

                break;
            }

            case Dominica:
            {
                LoadImage("_Dominica");

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.ENEMY;
                this.cost = 1;
                SetHitCount(1);

                break;
            }
        }

        if (formID != Form.Default)
        {
            CombatStats.onEndOfTurn.SubscribeOnce(this);
        }

        this.setCostForTurn(cost);
    }
}

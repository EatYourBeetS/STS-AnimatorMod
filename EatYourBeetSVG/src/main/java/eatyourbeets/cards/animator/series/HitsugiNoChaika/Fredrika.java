package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnLastSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.CounterAttackPower;
import eatyourbeets.utilities.*;

public class Fredrika extends AnimatorCard implements OnEndOfTurnLastSubscriber
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

    private static final int SPECIAL = 2;
    private static boolean flipVfx;
    private Form currentForm = Form.Default;

    private Fredrika(Form form)
    {
        this();

        ChangeForm(form);
    }

    public Fredrika()
    {
        super(DATA);

        Initialize(7, 2, 2, 4);
        SetUpgrade(2, 2, 1);

        SetAffinity_Star(1);

        SetAttackType(EYBAttackType.Normal);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return currentForm == Form.Default ? super.GetCardPreview() : null;
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return new ColoredString(SPECIAL, Colors.Cream(1));
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return currentForm == Form.Cat ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return (currentForm == Form.Dominica) ? super.GetDamageInfo() : (currentForm == Form.Dragoon) ? super.GetDamageInfo().AddMultiplier(2) : null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return (currentForm == Form.Default || currentForm == Form.Dominica) ? super.GetBlockInfo() : null;
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() +
        ((currentForm == Form.Default && CombatStats.CanActivatedStarter()) ? (GameUtilities.GetEnemies(true).size() * SPECIAL) : 0);
    }

    @Override
    public void OnEndOfTurnLast(boolean isPlayer)
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
                info.TryActivateStarter();
                GameActions.Bottom.GainBlock(block);
                break;
            }

            case Cat:
            {
                GameActions.Bottom.GainTemporaryHP(magicNumber);
                GameActions.Bottom.SpendEnergy(999, true)
                .AddCallback(energy ->
                {//
                    GameActions.Bottom.ModifyAllInstances(uuid).AddCallback(energy + 1, (amount, c) -> BlockModifiers.For(c).Add(amount));
                });
                break;
            }

            case Dominica:
            {
                GameActions.Bottom.GainBlock(block);
                GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
                GameActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
                break;
            }

            case Dragoon:
            {
                for (int i = 0; i < 2; i++)
                {
                    GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
                    .SetVFX(true, false)
                    .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.WHITE, Color.VIOLET).FlipX(flipVfx ^= true).SetScale(1.4f)).duration);
                }

                GameActions.Bottom.GainForce(SPECIAL);
                GameActions.Bottom.GainMetallicize(SPECIAL);
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

                break;
            }

            case Cat:
            {
                LoadImage("_Cat");

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                this.type = CardType.SKILL;
                this.target = CardTarget.NONE;
                this.cost = 0;

                break;
            }

            case Dragoon:
            {
                LoadImage("_Dragoon");

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                this.cost = 2;

                break;
            }

            case Dominica:
            {
                LoadImage("_Dominica");

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);
                this.type = CardType.ATTACK;
                this.target = CardTarget.ENEMY;
                this.cost = 1;

                break;
            }
        }

        if (formID != Form.Default && inBattle)
        {
            CombatStats.onEndOfTurnLast.SubscribeOnce(this);
        }

        this.setCostForTurn(cost);
    }
}

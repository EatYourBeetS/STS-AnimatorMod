package eatyourbeets.cards.animator.series.Elsword;

import basemod.abstracts.CustomSavable;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Elesis extends AnimatorCard implements CustomSavable<Elesis.Form>
{
    public enum Form
    {
        None,
        Saber,
        Pyro,
        Dark,
    }

    public static final EYBCardData DATA = Register(Elesis.class)
            .SetAttack(-2, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Elesis(Form.Saber, false), true);
                data.AddPreview(new Elesis(Form.Pyro, false), true);
                data.AddPreview(new Elesis(Form.Dark, false), true);
            });

    private Form currentForm;

    public Elesis()
    {
        this(Form.None, false);
    }

    private Elesis(Form form, boolean upgraded)
    {
        super(DATA);

        this.upgraded = upgraded;
        ChangeForm(form);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return currentForm != Form.None ? null : super.GetCardPreview();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (currentForm == Form.Saber)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> DamageModifiers.For(c).Add(magicNumber));
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (currentForm == Form.Saber)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> DamageModifiers.For(c).Add(magicNumber));
        }
        else if (currentForm == Form.Pyro && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        switch (currentForm)
        {
            case Saber:
            {
                GameActions.Bottom.GainForce(secondaryValue);
                GameActions.Bottom.GainBlessing(secondaryValue);
                break;
            }

            case Pyro:
            {
                GameActions.Bottom.ApplyBurning(p, m, GameUtilities.GetDebuffsCount(m.powers) * magicNumber).SkipIfZero(true);
                if (CheckSpecialCondition(true))
                {
                    GameActions.Bottom.Draw(2);
                }
                break;
            }

            case Dark:
            {
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
                break;
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        return currentForm == Form.None ? new Elesis(Form.None, upgraded) : super.makeStatEquivalentCopy();
    }

    @Override
    public AbstractCard makeCopy()
    {
        if (currentForm == Form.None && GameUtilities.InBattle(true) && GameUtilities.GetMasterDeckInstance(uuid) == null)
        {
            int roll = rng.random(0, 2);
            if (roll == 0)
            {
                return new Elesis(Form.Saber, false);
            }
            else if (roll == 1)
            {
                return new Elesis(Form.Pyro, false);
            }
            else
            {
                return new Elesis(Form.Dark, false);
            }
        }

        return new Elesis(currentForm, false);
    }

    @Override
    public Form onSave()
    {
        return currentForm;
    }

    @Override
    public void onLoad(Form form)
    {
        ChangeForm(form == null ? Form.None : form);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (currentForm == Form.Dark && startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Delayed.LoseHP(magicNumber, AttackEffects.SLASH_DIAGONAL).CanKill(false);
        }
        else if (currentForm == Form.None)
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new Elesis(Form.Saber, upgraded));
            group.group.add(new Elesis(Form.Pyro, upgraded));
            group.group.add(new Elesis(Form.Dark, upgraded));

            GameActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                if (cards != null && cards.size() > 0)
                {
                    Elesis card = (Elesis) cards.get(0);

                    ChangeForm(card.currentForm);

                    final AbstractCard inDeck = GameUtilities.GetMasterDeckInstance(uuid);
                    if (inDeck != null)
                    {
                        ((Elesis) inDeck).ChangeForm(currentForm);
                    }
                }
            });
        }
    }

    protected void ChangeForm(Form form)
    {
        if (this.currentForm == form)
        {
            return;
        }

        this.currentForm = form;

        switch (form)
        {
            case None:
            {
                LoadImage(null);

                affinities.Clear();
                SetAffinity_Star(2);

                cardText.OverrideDescription(null, true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = -2;

                break;
            }

            case Saber:
            {
                LoadImage("_Saber");

                Initialize(8, 0, 5, 3);
                SetUpgrade(0, 0, 3, 0);
                SetExhaust(true);

                affinities.Clear();
                SetAffinity_Red(1, 0, 3);
                SetAffinity_Green(1);
                SetAffinity_Light(2, 0, 6);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 2;

                break;
            }

            case Pyro:
            {
                LoadImage("_Pyro");

                Initialize(3, 0, 2);
                SetUpgrade(4, 0, 0);

                affinities.Clear();
                SetAffinity_Red(1, 0, 1);
                SetAffinity_Green(2, 0, 2);
                SetAffinityRequirement(Affinity.Green, 1);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

                break;
            }

            case Dark:
            {
                LoadImage("_Dark");

                Initialize(6, 0, 2);
                SetUpgrade(3, 0, 0);

                affinities.Clear();
                SetAffinity_Red(2, 0, 1);
                SetAffinity_Dark(2, 0, 2);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 0;

                break;
            }
        }

        if (upgraded)
        {
            upgraded = false;
            upgrade();
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return currentForm == Form.Saber
             ? super.CheckSpecialConditionSemiLimited(tryUse, super::CheckSpecialCondition)
             : super.CheckSpecialCondition(tryUse);
    }
}
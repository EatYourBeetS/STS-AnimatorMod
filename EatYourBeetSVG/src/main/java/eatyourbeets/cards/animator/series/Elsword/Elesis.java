package eatyourbeets.cards.animator.series.Elsword;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Elesis extends AnimatorCard implements CustomSavable<Elesis.Form>, StartupCard
{
    public enum Form
    {
        None,
        Saber,
        Pyro,
        Dark,
    }

    public static final EYBCardData DATA = Register(Elesis.class).SetAttack(-2, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Elesis(Form.Saber, false), true);
        DATA.AddPreview(new Elesis(Form.Pyro, false), true);
        DATA.AddPreview(new Elesis(Form.Dark, false), true);
    }

    private Form currentForm;
    private int bonusDamage = 0;

    private Elesis(Form form, boolean upgraded)
    {
        super(DATA);

        SetSynergy(Synergies.Elsword);
        this.upgraded = upgraded;
        ChangeForm(form);
    }

    public Elesis()
    {
        this(Form.None, false);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        if (currentForm != Form.None)
        {
            return null;
        }

        return super.GetCardPreview();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (currentForm == Form.Saber)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c ->
            {
                ((Elesis)c).AddDamageBonus(magicNumber);
                c.applyPowers();
            });
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (currentForm == Form.Saber)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> ((Elesis)c).AddDamageBonus(magicNumber));
        }
        else if (currentForm == Form.Pyro && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        switch (currentForm)
        {
            case Saber:
            {
                GameActions.Bottom.SpendEnergy(999, true).AddCallback(amount ->
                {
                    GameActions.Bottom.GainForce(amount);
                    GameActions.Bottom.GainAgility(amount);
                });
                AddDamageBonus(-bonusDamage);
                break;
            }

            case Pyro:
            {
                GameActions.Bottom.ApplyBurning(p, m, GameUtilities.GetDebuffsCount(m.powers)).SkipIfZero(true);
                if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
                {
                    GameActions.Bottom.Draw(1);
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

                cardText.OverrideDescription(null, true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = -2;

                break;
            }

            case Saber:
            {
                LoadImage("_Saber");

                Initialize(3, 0, 5);
                SetUpgrade(0, 0, 2);
                SetScaling(0, 1, 1);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

                break;
            }

            case Pyro:
            {
                LoadImage("_Pyro");

                Initialize(6, 0, 0);
                SetUpgrade(4, 0, 0);
                SetScaling(0, 1, 0);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

                break;
            }

            case Dark:
            {
                LoadImage("_Dark");

                Initialize(9, 0, 3);
                SetUpgrade(0, 0, -1);
                SetScaling(0, 0, 2);

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
    public AbstractCard makeCopy()
    {
        if (currentForm == Form.None && GameUtilities.InBattle() && GameUtilities.GetMasterDeckInstance(uuid) == null)
        {
            int roll = rng.random(0, 2);
            if (roll == 0)
            {
                return new Elesis(Form.Saber, upgraded);
            }
            else if (roll == 1)
            {
                return new Elesis(Form.Pyro, upgraded);
            }
            else
            {
                return new Elesis(Form.Dark, upgraded);
            }
        }

        return new Elesis(currentForm, upgraded);
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
    public boolean atBattleStartPreDraw()
    {
        if (currentForm == Form.Dark)
        {
            GameActions.Bottom.LoseHP(magicNumber, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

            return true;
        }
        else if (currentForm == Form.None)
        {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new Elesis(Form.Saber, upgraded));
            group.group.add(new Elesis(Form.Pyro, upgraded));
            group.group.add(new Elesis(Form.Dark, upgraded));

            GameActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .SetMessage(CardRewardScreen.TEXT[1])
            .AddCallback(cards ->
            {
                if (cards != null && cards.size() > 0)
                {
                    Elesis card = (Elesis) cards.get(0);

                    ChangeForm(card.currentForm);

                    AbstractCard inDeck = GameUtilities.GetMasterDeckInstance(uuid);
                    if (inDeck != null)
                    {
                        ((Elesis) inDeck).ChangeForm(currentForm);
                    }
                }
            });

            return true;
        }

        return false;
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}
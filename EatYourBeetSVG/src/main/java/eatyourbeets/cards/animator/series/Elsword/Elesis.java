package eatyourbeets.cards.animator.series.Elsword;

import basemod.abstracts.CustomSavable;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
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
    private int bonusDamage = 0;

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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        switch (currentForm)
        {
            case Saber:
            {
                GameActions.Bottom.SpendEnergy(999, true).AddCallback(amount ->
                {
                    GameActions.Bottom.GainForce(amount * 2);
                    GameActions.Bottom.GainBlessing(amount * 2);
                });
                AddDamageBonus(-bonusDamage);
                break;
            }

            case Pyro:
            {
                GameActions.Bottom.ApplyBurning(p, m, GameUtilities.GetDebuffsCount(m.powers) * magicNumber).SkipIfZero(true);
                if (HasSynergy() && info.TryActivateSemiLimited())
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

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        return currentForm == Form.None ? makeCopy() : super.makeStatEquivalentCopy();
    }

    @Override
    public AbstractCard makeCopy()
    {
        if (currentForm == Form.None && GameUtilities.InBattle(true) && GameUtilities.GetMasterDeckInstance(uuid) == null)
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

                Initialize(4, 0, 5);
                SetUpgrade(0, 0, 2);

                affinities.Clear();
                SetAffinity_Red(1, 0, 1);
                SetAffinity_Green(1);
                SetAffinity_Light(2, 0, 3);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

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

                affinities.Clear();
                SetAffinity_Red(2);
                SetAffinity_Dark(2);

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

    protected void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}
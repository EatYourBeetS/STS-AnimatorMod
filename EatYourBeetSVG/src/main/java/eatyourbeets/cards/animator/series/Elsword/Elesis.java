package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Elesis extends AnimatorCard
{
    public static final int FORM_NONE = 0;
    public static final int FORM_SABER = 1;
    public static final int FORM_PYRO = 2;
    public static final int FORM_DARK = 3;
    public static final EYBCardData DATA = Register(Elesis.class)
            .SetAttack(-2, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .SetMultiformData(4, false, false, false, true)
            .PostInitialize(data ->
            {
                data.AddPreview(new Elesis(1, 0), true);
                data.AddPreview(new Elesis(2, 0), true);
                data.AddPreview(new Elesis(3, 0), true);
            });

    private int bonusDamage = 0;

    public Elesis()
    {
        this(FORM_NONE, 0);
    }

    private Elesis(int form, int timesUpgraded)
    {
        super(DATA, form, timesUpgraded);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return auxiliaryData.form != FORM_NONE ? null : super.GetCardPreview();
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (auxiliaryData.form == 1)
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

        if (auxiliaryData.form == FORM_SABER)
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> ((Elesis)c).AddDamageBonus(magicNumber));
        }
        else if (auxiliaryData.form == FORM_PYRO && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        switch (auxiliaryData.form)
        {
            case FORM_SABER:
            {
                GameActions.Bottom.SpendEnergy(999, true).AddCallback(amount ->
                {
                    GameActions.Bottom.GainForce(amount * 2);
                    GameActions.Bottom.GainAgility(amount * 2);
                });
                AddDamageBonus(-bonusDamage);
                break;
            }

            case FORM_PYRO:
            {
                GameActions.Bottom.ApplyBurning(p, m, GameUtilities.GetDebuffsCount(m.powers) * magicNumber).SkipIfZero(true);
                if (HasSynergy() && info.TryActivateSemiLimited())
                {
                    GameActions.Bottom.Draw(1);
                }
                break;
            }

            case FORM_DARK:
            {
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
                break;
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        return auxiliaryData.form == FORM_NONE ? makeCopy() : super.makeStatEquivalentCopy();
    }

    @Override
    public AbstractCard makeCopy()
    {
        if (auxiliaryData.form == FORM_NONE && GameUtilities.InBattle(true) && GameUtilities.GetMasterDeckInstance(uuid) == null)
        {
            Elesis e = new Elesis(rng.random(1, 3), timesUpgraded);
            if (upgraded) {
                e.upgrade();
            }
        }

        return new Elesis(auxiliaryData.form, timesUpgraded);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (auxiliaryData.form == FORM_DARK && startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Delayed.LoseHP(magicNumber, AttackEffects.SLASH_DIAGONAL).CanKill(false);
        }
        else if (auxiliaryData.form == FORM_NONE)
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new Elesis(FORM_SABER, timesUpgraded));
            group.group.add(new Elesis(FORM_PYRO, timesUpgraded));
            group.group.add(new Elesis(FORM_DARK, timesUpgraded));

            GameActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                if (cards != null && cards.size() > 0)
                {
                    Elesis card = (Elesis) cards.get(0);

                    SetForm(card.auxiliaryData.form, timesUpgraded);

                    final AbstractCard inDeck = GameUtilities.GetMasterDeckInstance(uuid);
                    if (inDeck != null)
                    {
                        ((Elesis) inDeck).SetForm(auxiliaryData.form, timesUpgraded);
                    }
                }
            });
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded)
    {
        //if (this.auxiliaryData.form == form)
        //{
        //    return super.SetForm(form, timesUpgraded);
        //}

        switch (form)
        {
            case FORM_NONE:
            {
                LoadImage(null);

                affinities.Clear();
                SetAffinity_Star(2);

                cardText.OverrideDescription(null, true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = -2;

                break;
            }

            case FORM_SABER:
            {
                LoadImage("_Saber");

                Initialize(3, 0, 5);
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

            case FORM_PYRO:
            {
                LoadImage("_Pyro");

                Initialize(3, 0, 2);
                SetUpgrade(4, 0, 0);

                affinities.Clear();
                SetAffinity_Red(1);
                SetAffinity_Green(2, 0, 1);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

                break;
            }

            case FORM_DARK:
            {
                LoadImage("_Dark");

                Initialize(9, 0, 3);
                SetUpgrade(0, 0, -1);

                affinities.Clear();
                SetAffinity_Red(2, 0, 2);
                SetAffinity_Dark(2, 0, 2);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 0;

                break;
            }
        }

        if (timesUpgraded > 0)
        {
            upgraded = false;
            upgrade();
        }
        return super.SetForm(form, timesUpgraded);
    }

    protected void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}
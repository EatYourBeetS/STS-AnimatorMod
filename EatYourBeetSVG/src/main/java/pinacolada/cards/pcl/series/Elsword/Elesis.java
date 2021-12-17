package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.EYBCardPreview;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Elesis extends PCLCard
{
    public static final int FORM_NONE = 0;
    public static final int FORM_SABER = 1;
    public static final int FORM_PYRO = 2;
    public static final int FORM_DARK = 3;
    public static final PCLCardData DATA = Register(Elesis.class)
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

        if (auxiliaryData.form == FORM_SABER)
        {
            PCLActions.Bottom.ModifyAllInstances(uuid, c ->
            {
                ((Elesis)c).AddDamageBonus(magicNumber);
                c.applyPowers();
            });
            PCLActions.Bottom.Flash(this);
        }
        else if (auxiliaryData.form == FORM_DARK) {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.SLASH_VERTICAL);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (auxiliaryData.form == FORM_SABER)
        {
            PCLActions.Bottom.ModifyAllInstances(uuid, c -> ((Elesis)c).AddDamageBonus(magicNumber));
        }
        else if (auxiliaryData.form == FORM_PYRO && CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.Draw(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        switch (auxiliaryData.form)
        {
            case FORM_SABER:
            {
                PCLActions.Bottom.SpendEnergy(999, true).AddCallback(amount ->
                {
                    PCLActions.Bottom.GainMight(amount * 2);
                    PCLActions.Bottom.GainVelocity(amount * 2);
                });
                AddDamageBonus(-bonusDamage);
                break;
            }

            case FORM_PYRO:
            {
                PCLActions.Bottom.ApplyBurning(p, m, PCLGameUtilities.GetDebuffsCount(m.powers) * magicNumber).SkipIfZero(true);
                if (HasSynergy() && info.TryActivateSemiLimited())
                {
                    PCLActions.Bottom.Draw(1);
                }
                break;
            }

            case FORM_DARK:
            {
                PCLActions.Bottom.ApplyVulnerable(p, m, 1);
                int toTransfer = Math.min(magicNumber, PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID));
                PCLActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, toTransfer);
                AbstractMonster mo = PCLGameUtilities.GetRandomEnemy(true);
                if (mo != null && toTransfer > 0) {
                    PCLActions.Bottom.DealDamageAtEndOfTurn(player, mo, toTransfer, AttackEffects.CLAW);
                }

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
        if (auxiliaryData.form == FORM_NONE && PCLGameUtilities.InBattle(true) && PCLGameUtilities.GetMasterDeckInstance(uuid) == null)
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

        if (auxiliaryData.form == FORM_NONE)
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new Elesis(FORM_SABER, timesUpgraded));
            group.group.add(new Elesis(FORM_PYRO, timesUpgraded));
            group.group.add(new Elesis(FORM_DARK, timesUpgraded));

            PCLActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                if (cards != null && cards.size() > 0)
                {
                    Elesis card = (Elesis) cards.get(0);

                    SetForm(card.auxiliaryData.form, timesUpgraded);

                    final AbstractCard inDeck = PCLGameUtilities.GetMasterDeckInstance(uuid);
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
                SetAffinity_Red(1);

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
                SetAffinity_Light(1, 0, 3);

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
                SetAffinity_Red(1, 0, 2);
                SetAffinity_Green(1, 0, 1);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

                break;
            }

            case FORM_DARK:
            {
                LoadImage("_Dark");

                Initialize(8, 0, 9, 6);
                SetUpgrade(2, 0, 4);

                affinities.Clear();
                SetAffinity_Red(1, 0, 2);
                SetAffinity_Dark(1, 0, 2);

                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);
                this.isCostModified = this.isCostModifiedForTurn = false;
                this.cost = this.costForTurn = 1;

                break;
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    protected void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}
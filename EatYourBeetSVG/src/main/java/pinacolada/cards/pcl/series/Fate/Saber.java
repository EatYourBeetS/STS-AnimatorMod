package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Saber_Alter;
import pinacolada.cards.pcl.special.Saber_Excalibur;
import pinacolada.cards.pcl.special.Saber_X;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class Saber extends PCLCard
{
    private static final PCLAffinity[] cardAffinities = new PCLAffinity[] {PCLAffinity.Light, PCLAffinity.Blue, PCLAffinity.Green};
    private static final ArrayList<FuncT0<PCLCard>> cardConstructors = new ArrayList<>();
    public static final ArrayList<PCLCardPreview> cardPreviews = new ArrayList<>();
    public static final PCLCardData DATA = Register(Saber.class)
            .SetAttack(1, CardRarity.RARE, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetMultiformData(3, false)
            .SetSeriesFromClassPackage()
            .PostInitialize(
                    data -> {
                        for (FuncT0<PCLCard> constructor : cardConstructors) {
                            cardPreviews.add(new PCLCardPreview(constructor.Invoke(), false));
                        }
                    }
            );

    static {
        cardConstructors.add(Saber_Excalibur::new);
        cardConstructors.add(Saber_Alter::new);
        cardConstructors.add(Saber_X::new);
    }

    public Saber()
    {
        super(DATA);

        Initialize(7, 2, 1, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1, 0, 1);

        SetSoul(7, 0, Saber_Excalibur::new);
        SetLoyal(true);
        SetUnique(true, -1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        SetSoul(7, 0, cardConstructors.get(form));
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public PCLCardPreview GetCardPreview()
    {
        return cardPreviews.get(auxiliaryData.form);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form], cardAffinities[auxiliaryData.form].GetFormattedPowerSymbol());
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetInnate(true);
        if (timesUpgraded % 4 == 1)
        {
            upgradeBlock(1);
            upgradeDamage(1);
        }
        else
        {
            upgradeMagicNumber(0);
            upgradeDamage(2);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.StackAffinityPower(cardAffinities[auxiliaryData.form], secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        PCLActions.Bottom.GainBlock(block);
        this.baseDamage += magicNumber;
        cooldown.ProgressCooldownAndTrigger(m);
    }
}
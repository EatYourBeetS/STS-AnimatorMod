package pinacolada.cards.pcl.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Aisha extends PCLCard
{
    public static final PCLCardData DATA = Register(Aisha.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Electric)
            .SetMultiformData(2, false)
            .SetSeries(CardSeries.Elsword);


    public Aisha()
    {
        super(DATA);

        Initialize(2, 0, 1, 1);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Blue(2, 0, 1);
        SetHitCount(2);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
        else {
            this.cardText.OverrideDescription(null, true);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (player.filledOrbCount() * secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(enemy ->
                {
                    PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PURPLE));
                    return PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.VIOLET)).duration * 0.1f;
                }));

        if (IsStarter())
        {
            PCLActions.Bottom.TriggerOrbPassive(1)
            .SetFilter(o -> o.ID.equals(auxiliaryData.form == 1 ? Dark.ORB_ID : Lightning.ORB_ID))
            .AddCallback(orbs ->
            {
                if (orbs.size() > 0)
                {
                    PCLActions.Bottom.StackAffinityPower(auxiliaryData.form == 1 ? PCLAffinity.Dark : PCLAffinity.Blue, magicNumber, false);
                }
                else {
                    PCLActions.Bottom.ChannelOrb(auxiliaryData.form == 1 ? new Dark() : new Lightning());
                }
            });
        }
    }
}
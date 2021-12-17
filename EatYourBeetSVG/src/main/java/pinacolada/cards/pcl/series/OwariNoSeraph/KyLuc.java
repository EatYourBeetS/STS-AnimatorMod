package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class KyLuc extends PCLCard
{
    public static final PCLCardData DATA = Register(KyLuc.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Normal)
            .SetSeriesFromClassPackage();

    public KyLuc()
    {
        super(DATA);

        Initialize(7, 0, 3, 3);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Dark(1, 0, 2);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) * magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractCreature c: PCLGameUtilities.GetAllCharacters(true)) {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, c, secondaryValue, AttackEffects.SLASH_VERTICAL);
        }

        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d
                .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.RED, Color.LIGHT_GRAY, Color.RED, Color.RED).duration * 0.6f));
    }
}
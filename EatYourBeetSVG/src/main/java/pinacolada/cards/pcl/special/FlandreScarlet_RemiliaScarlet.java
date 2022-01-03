package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class FlandreScarlet_RemiliaScarlet extends PCLCard
{
    public static final PCLCardData DATA = Register(FlandreScarlet_RemiliaScarlet.class).SetAttack(1, CardRarity.SPECIAL).SetSeries(CardSeries.TouhouProject);

    public FlandreScarlet_RemiliaScarlet()
    {
        super(DATA);

        Initialize(4, 0, 3, 2);
        SetUpgrade(2, 0, 0, 1);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1);


        SetEthereal(true);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 6);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + magicNumber * PCLJUtils.Count(player.hand.group, c -> c.type == CardType.ATTACK));
    }

    public AbstractAttribute GetSpecialInfo() {
        return damage > 0 ? TempHPAttribute.Instance.SetCard(this).SetText(damage, Settings.CREAM_COLOR) : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BITE);
        if (damage > 0)
        {
            PCLActions.Bottom.GainTemporaryHP(damage);
        }
        boolean isTemporary = TrySpendAffinity(PCLAffinity.Light);
        PCLActions.Delayed.SelectFromHand(name, player.hand.size() - 1, true).SetFilter(c -> c.uuid != this.uuid && c.type == CardType.ATTACK).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                PCLGameUtilities.ModifyDamage(c, magicNumber, isTemporary);
            }
        });
    }
}


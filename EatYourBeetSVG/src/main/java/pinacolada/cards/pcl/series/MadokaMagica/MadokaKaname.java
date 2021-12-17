package pinacolada.cards.pcl.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.pcl.special.MadokaKaname_Krimheild;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class MadokaKaname extends PCLCard
{
    public static final PCLCardData DATA = Register(MadokaKaname.class)
            .SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new MadokaKaname_Krimheild(), true);
            });

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetHealing(true);
        SetExhaust(true);
        SetProtagonist(true);
        SetHarmonic(true);

        SetSoul(2, 0, MadokaKaname_Krimheild::new);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return secondaryValue <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(secondaryValue, Colors.Cream(1f)));
    }

    @Override
    public int GetXValue() {
        return secondaryValue * (PCLJUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.hand.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.exhaustPile.group, c -> c.type == CardType.CURSE));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.RecoverHP(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.PurgeFromPile(name, magicNumber, p.exhaustPile, player.hand, player.discardPile, player.drawPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(c -> CardType.CURSE.equals(c.type))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.HealPlayerLimited(this, secondaryValue * cards.size());
                PCLActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
            }
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}

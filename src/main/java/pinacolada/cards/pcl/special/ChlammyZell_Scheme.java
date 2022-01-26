package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.series.NoGameNoLife.ChlammyZell;
import pinacolada.powers.special.ChlammyZellPower;
import pinacolada.utilities.PCLActions;

public class ChlammyZell_Scheme extends PCLCard
{
    public static final PCLCardData DATA = Register(ChlammyZell_Scheme.class)
            .SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(ChlammyZell.DATA.Series);

    public ChlammyZell_Scheme()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY + p.hb.height), 0.15f);
        PCLActions.Bottom.StackPower(new ChlammyZellPower(p, magicNumber));
    }
}
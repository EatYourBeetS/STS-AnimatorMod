package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;

public class HiiragiMahiru extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(HiiragiMahiru.class)
            .SetSkill(4, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.OwariNoSeraph);

    public HiiragiMahiru()
    {
        super(DATA);

        Initialize(0, 0, 20);
        SetUpgrade(0, 0, 10);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.MoveCards(player.exhaustPile, player.drawPile).ShowEffect(false, false);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        for (AbstractCard c : p.discardPile.group)
        {
            PCLActions.Bottom.PlayCard(c, p.discardPile, m).SetExhaust(true);
        }
    }
}
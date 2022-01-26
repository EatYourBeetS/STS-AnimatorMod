package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;

public class Chomusuke extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Chomusuke.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Konosuba);

    public Chomusuke()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.GainEnergy(2);
            PCLActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
            .ShowEffect(true, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(1);
    }
}
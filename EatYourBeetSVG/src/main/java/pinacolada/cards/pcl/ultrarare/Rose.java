package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.special.RoseDamageAction;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Rose extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Rose.class)
            .SetAttack(3, CardRarity.SPECIAL, PCLAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Elsword);

    public Rose()
    {
        super(DATA);

        Initialize(10, 0, 2, 40);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Green(0,0,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(magicNumber);
        PCLActions.Bottom.Reload(name, m, (enemy, cards) ->
        {
            if (enemy != null && !PCLGameUtilities.IsDeadOrEscaped(enemy))
            {
                PCLActions.Bottom.Add(new RoseDamageAction(enemy, this, cards.size() + 1, damage));
            }
        });
    }
}
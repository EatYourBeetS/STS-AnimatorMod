package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class RinKaenbyou_VengefulSpirit extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(RinKaenbyou_VengefulSpirit.class)
    		.SetCurse(0, PCLCardTarget.None, true)
            .SetSeries(CardSeries.TouhouProject);

    public RinKaenbyou_VengefulSpirit()
    {
        super(DATA, false);

        Initialize(0, 0, 6, 3);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAutoplay(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealDamageToRandomEnemy(magicNumber, DamageInfo.DamageType.HP_LOSS, AttackEffects.DARKNESS);
        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.FIRE);
    }
}

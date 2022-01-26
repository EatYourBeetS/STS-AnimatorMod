package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.utilities.PCLActions;

public class Crystallize extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(Crystallize.class)
            .SetStatus(1, CardRarity.UNCOMMON, PCLCardTarget.None);

    public Crystallize()
    {
        super(DATA, false);

        Initialize(0, 0, 4, 3);

        SetAffinity_Silver(1);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            PCLActions.Bottom.SFX(SFX.ORB_FROST_EVOKE, 1f, 1.2f);
            PCLActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL);
            PCLActions.Bottom.GainMetallicize(magicNumber);
            PCLActions.Bottom.LoseHP(secondaryValue, AttackEffects.SLASH_VERTICAL);
            PCLActions.Bottom.GainTechnic(1, false);
        }
    }
}
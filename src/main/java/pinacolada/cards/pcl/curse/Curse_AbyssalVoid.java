package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_AbyssalVoid extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_AbyssalVoid.class).SetCurse(-1, PCLCardTarget.None, true);

    public Curse_AbyssalVoid()
    {
        super(DATA, false);

        Initialize(0, 0, 6, 3);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Dark(1);

        SetAutoplay(true);
        SetPurge(true);
        SetEthereal(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);
        PCLActions.Bottom.GainDesecration(magicNumber * stacks);
        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player,secondaryValue * stacks, AttackEffects.DARKNESS);
    }
}